package hieuboy.developer.restcontrollers;

import hieuboy.developer.models.*;
import hieuboy.developer.services.ICategoryService;
import hieuboy.developer.services.IManufacturerService;
import hieuboy.developer.services.IProductService;
import hieuboy.developer.utils.Value;
import hieuboy.developer.utils.ValueAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductRestController {

    private static final Logger log4j = LoggerFactory.getLogger(ProductRestController.class);

    private IProductService productService;

    private ICategoryService categoryService;

    private IManufacturerService manufacturerService;

    public ProductRestController(IProductService productService,
                                 ICategoryService categoryService,
                                 IManufacturerService manufacturerService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
    }

    @GetMapping(value = ValueAPI.PRODUCT_ADMIN, produces = "application/json")
    public ModelAndView getListProduct() {
        ModelAndView mav = new ModelAndView("admin/views/front_end/product/product");
        try {
            ProductModel model = new ProductModel();
            model.setPageIndex(0);
            model.setPageSize(Value.PAGE_SIZE);
            model.setDesending(1);
            model.setColumn("id");
            model.setCategoryID(0);
            model.setManufacturerID(0);
            model.setCode(null);
            model.setStatus(0);
            List<ManufacturerModel> lstManufacturer = manufacturerService.getAllManufacturer();
            mav.addObject("lstManufacturer", lstManufacturer);

            List<CategoryModel> lstCategory = categoryService.getAllCategory();
            mav.addObject("lstCategory", lstCategory);

            AjaxResultPagingModel pagingModel = productService.getListProductModel(model);
            mav.addObject("lstProduct", pagingModel);

            mav.addObject("active", "product");
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.PRODUCT_API + ValueAPI.LOAD_DATA, produces = "application/json")
    public ModelAndView loadDataProduct(@ModelAttribute ProductModel model) {
        ModelAndView mav = new ModelAndView("admin/views/front_end/product/product_load_data");
        try {
            if (model.getCode().equals("")) model.setCode(null);
            else model.setCode(model.getCode());
            if (model.getColumn().equals("")) model.setColumn("id");
            model.setDesending(model.getDesending());
            model.setPageIndex(model.getPageIndex() - 1);
            model.setPageSize(model.getPageSize());

            AjaxResultPagingModel pagingModel = productService.getListProductModel(model);
            mav.addObject("lstProduct", pagingModel);

            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.PRODUCT_API + ValueAPI.GET_DETAIL_DATA, produces = "application/json")
    public AjaxResultModel<ProductModel> getDetailProduct(@PathVariable Integer id) {
        AjaxResultModel<ProductModel> result = new AjaxResultModel<>();
        try {
            ProductModel model = productService.getDetailProduct(id);
            result.setResult(true);
            result.setResultData(model);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT FIND ID PRODUCT: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @PostMapping(value = ValueAPI.PRODUCT_API + ValueAPI.SAVE_DATA, produces = "application/json")
    public AjaxResult saveDataProduct(@ModelAttribute ProductModel model,
                                      @RequestParam("file") Optional<MultipartFile> file) {
        AjaxResult result = new AjaxResult();
        try {
            boolean check = productService.saveProduct(model, file);
            result.setResult(check);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT SAVE DATA PRODUCT: " + e.getMessage());
            return result;
        }
    }

    @PutMapping(value = ValueAPI.PRODUCT_API + ValueAPI.UPDATE_DATA, produces = "application/json")
    public AjaxResult updateProduct(@ModelAttribute ProductModel model,
                                    @RequestParam("file") Optional<MultipartFile> file) {
        AjaxResult result = new AjaxResult();
        try {
            int code = productService.updateProduct(model, file);
            result.setCode(code);
            result.setResult(true);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT UPDATE PRODUCT: " + e.getMessage());
            return result;
        }
    }

    @DeleteMapping(value = ValueAPI.PRODUCT_API + ValueAPI.DELETE_DATA, produces = "application/json")
    public boolean deleteDataProduct(@PathVariable Integer id) {
        try {
            return productService.deleteProduct(id);
        } catch (Exception e) {
            log4j.error("CAN NOT DELETE DATA PRODUCT: " + e.getMessage());
            return false;
        }
    }

    @PostMapping(value = ValueAPI.PRODUCT_API_CHECK_NAME, produces = "application/json")
    public boolean checkNameProduct(String name) {
        try {
            return productService.checkNameProduct(name);
        } catch (Exception e) {
            log4j.error("CAN NOT FIND NAME: " + e.getMessage());
            return false;
        }
    }

}
