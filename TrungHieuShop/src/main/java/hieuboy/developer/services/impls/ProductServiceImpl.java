package hieuboy.developer.services.impls;

import hieuboy.developer.entities.Product;
import hieuboy.developer.entities.User;
import hieuboy.developer.mappers.ProductMapper;
import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.ProductModel;
import hieuboy.developer.repositories.ProductRepository;
import hieuboy.developer.repositories.UserRepository;
import hieuboy.developer.services.IProductService;
import hieuboy.developer.utils.AuthenticationHelper;
import hieuboy.developer.utils.Common;
import hieuboy.developer.utils.HelperService;
import hieuboy.developer.utils.Value;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    private static final Logger log4j = LoggerFactory.getLogger(ProductServiceImpl.class);

    private ProductRepository productRepository;

    private UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ProductModel> getAllProduct() {
        return ProductMapper.MAPPER.mapListEntityToListModel(productRepository.findAll().stream());
    }

    @Override
    public AjaxResultPagingModel getListProductModel(ProductModel model) {
        Sort sort = new Sort(model.getDesending() == 1 ? Sort.Direction.DESC : Sort.Direction.ASC, model.getColumn());
        Pageable pageable = PageRequest.of(model.getPageIndex(), model.getPageSize(), sort);
        Page<Product> lstProduct = productRepository.getListProductAndSearch(model.getName(), model.getCode(),
                model.getStatus(), model.getCategoryID(), model.getManufacturerID(), pageable);
        return new HelperService().pagingModel(model.getColumn(), model.getDesending(), model.getPageIndex(),
                model.getPageSize(), lstProduct.getTotalPages(), lstProduct.getTotalElements(),
                ProductMapper.MAPPER.mapListEntityToListModel(lstProduct.getContent().stream()));
    }

    @Override
    @Transactional
    public boolean saveProduct(ProductModel model, Optional<MultipartFile> file) {
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_");
            Product product = ProductMapper.MAPPER.mapModelToEntity(model);
            // Upload file lên server
            uploadFileService(file, calendar, simpleDateFormat, product);
            product.setDatecreated(calendar.getTime());
            product.setCategoryID(model.getCategoryID());
            product.setManufacturerID(model.getManufacturerID());
            User user = new AuthenticationHelper(userRepository).getUsernameLoginInfo();
            if (user != null)
                product.setUsercreated(user.getUsername());
            productRepository.save(product);
            product.setCode(genProductCode(product));
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }


    @Override
    @Transactional
    public int updateProduct(ProductModel model, Optional<MultipartFile> file) {
        try {
            Optional<Product> productOptional = productRepository.findById(model.getId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                String fileImage = product.getImageLink();
                ProductMapper.MAPPER.updateFromDTOToEntity(model, product);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_");
                if (file.isPresent()) {
                    String filePath = file.get().getOriginalFilename();
                    if (filePath != null)
                        uploadFileService(file, calendar, simpleDateFormat, product);
                } else product.setImageLink(fileImage);
                productRepository.save(product);
                return 1;
            }
            return 2;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return 0;
        }
    }

    @Override
    @Transactional
    public boolean deleteProduct(Integer id) {
        try {
            productRepository.deleteByProductId(id);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkNameProduct(String name) {
        return productRepository.findByName(name) == null;
    }

    @Override
    public ProductModel getDetailProduct(Integer id) {
        try {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent())
                return ProductMapper.MAPPER.mapEntityToModel(product.get());
        } catch (Exception e) {
            log4j.error(e.getMessage());
        }
        return null;
    }

    /**
     * Gen mã cho sản phẩm
     */
    private String genProductCode(Product product) {
        String productCode = "SP";
        // Thêm mã tự động tăng của khách hàng
        String codeAutoGen = StringUtils.leftPad(product.getId().toString(), 8, "0");
        productCode += codeAutoGen + Common.genCheckSumCode(codeAutoGen);
        return productCode;
    }

    private void uploadFileService(Optional<MultipartFile> file,
                                   Calendar calendar,
                                   SimpleDateFormat simpleDateFormat,
                                   Product product) throws Exception {
        // Upload file lên server
        if (file.isPresent()) {
            String workingDir = System.getProperty("user.dir");
            String absolutePath = workingDir + Value.PRODUCT_PATH_UPLOAD;
            File uploadRootDir = new File(absolutePath);
            if (!uploadRootDir.exists()) uploadRootDir.mkdirs();
            String fileName = simpleDateFormat
                    .format(calendar.getTime()).toLowerCase() + file.get().getOriginalFilename();
            File iofile = new File(absolutePath, fileName);
            file.get().transferTo(iofile);
            product.setImageLink(Value.PRODUCT_IMAGE_URL + fileName);
        }
    }
}
