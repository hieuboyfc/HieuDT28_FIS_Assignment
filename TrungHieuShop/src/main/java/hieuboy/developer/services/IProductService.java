package hieuboy.developer.services;

import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.ProductModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<ProductModel> getAllProduct();

    AjaxResultPagingModel getListProductModel(ProductModel model);

    boolean saveProduct(ProductModel model, Optional<MultipartFile> file);

    int updateProduct(ProductModel model, Optional<MultipartFile> file);

    boolean deleteProduct(Integer id);

    boolean checkNameProduct(String name);

    ProductModel getDetailProduct(Integer id);
}
