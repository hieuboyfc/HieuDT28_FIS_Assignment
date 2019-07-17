package hieuboy.developer.restcontrollers;

import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.ProductModel;
import hieuboy.developer.services.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeRestController {
    private static final Logger log4j = LoggerFactory.getLogger(ProductRestController.class);

    private IProductService productService;

    public HomeRestController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("login/login");
    }

    @GetMapping(value = "/admin/home")
    public ModelAndView publicAdmin() {
        ModelAndView mav = new ModelAndView("admin/views/front_end/home");
        mav.addObject("active", "home");
        return mav;
    }

    @GetMapping(value = "/public/home")
    public ModelAndView publicHome() {
        ModelAndView mav = new ModelAndView("public/views/front_end/home");
        try {
            ProductModel model = new ProductModel();
            model.setPageIndex(0);
            model.setPageSize(9);
            model.setDesending(1);
            model.setColumn("id");
            model.setCode(null);
            model.setStatus(0);
            AjaxResultPagingModel pagingModel = productService.getListProductModel(model);
            mav.addObject("lstProduct", pagingModel);

            mav.addObject("active", "public-home");
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = "/public/product-detail/{id}")
    public ModelAndView publicProductDetail(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("public/views/front_end/product_details");
        try {
            ProductModel product = productService.getDetailProduct(id);
            mav.addObject("product", product);
            mav.addObject("active", "public-product-detail");
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

}
