package hieuboy.developer.restcontrollers;

import hieuboy.developer.models.AjaxResult;
import hieuboy.developer.models.AjaxResultModel;
import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.CategoryModel;
import hieuboy.developer.services.ICategoryService;
import hieuboy.developer.utils.Value;
import hieuboy.developer.utils.ValueAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CategoryRestController {

    private static final Logger log4j = LoggerFactory.getLogger(CategoryRestController.class);

    private ICategoryService categoryService;

    public CategoryRestController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = ValueAPI.CATEGORY_ADMIN, produces = "application/json")
    public ModelAndView getListCategory() {
        ModelAndView mav = new ModelAndView("admin/views/front_end/category/category");
        try {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setPageIndex(0);
            categoryModel.setPageSize(Value.PAGE_SIZE);
            categoryModel.setDesending(1);
            categoryModel.setColumn("id");
            AjaxResultPagingModel pagingModel = categoryService.getListCategoryModel(categoryModel);
            mav.addObject("active", "category");
            mav.addObject("lstCategory", pagingModel);
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.CATEGORY_API + ValueAPI.LOAD_DATA, produces = "application/json")
    public ModelAndView loadDataCategory(@ModelAttribute CategoryModel categoryModel) {
        ModelAndView mav = new ModelAndView("admin/views/front_end/category/category_load_data");
        try {
            if (categoryModel.getColumn().equals("")) categoryModel.setColumn("id");
            categoryModel.setDesending(categoryModel.getDesending());
            categoryModel.setPageIndex(categoryModel.getPageIndex() - 1);
            categoryModel.setPageSize(categoryModel.getPageSize());
            AjaxResultPagingModel pagingModel = categoryService.getListCategoryModel(categoryModel);
            mav.addObject("lstCategory", pagingModel);
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.CATEGORY_API + ValueAPI.GET_DETAIL_DATA, produces = "application/json")
    public AjaxResultModel<CategoryModel> getDetailCategory(@PathVariable Integer id) {
        AjaxResultModel<CategoryModel> result = new AjaxResultModel<>();
        try {
            CategoryModel categoryModel = categoryService.getDetailCategory(id);
            result.setResult(true);
            result.setResultData(categoryModel);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT FIND ID CATEGORY: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @PostMapping(value = ValueAPI.CATEGORY_API + ValueAPI.SAVE_DATA, produces = "application/json")
    public AjaxResult saveDataCategory(@ModelAttribute CategoryModel categoryModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean check = categoryService.saveCategory(categoryModel);
            result.setResult(check);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT SAVE DATA CATEGORY: " + e.getMessage());
            return result;
        }
    }

    @PutMapping(value = ValueAPI.CATEGORY_API + ValueAPI.UPDATE_DATA, produces = "application/json")
    public AjaxResult updateCategory(@ModelAttribute CategoryModel categoryModel) {
        AjaxResult result = new AjaxResult();
        try {
            int code = categoryService.updateCategory(categoryModel);
            result.setCode(code);
            result.setResult(true);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT UPDATE CATEGORY: " + e.getMessage());
            return result;
        }
    }

    @DeleteMapping(value = ValueAPI.CATEGORY_API + ValueAPI.DELETE_DATA, produces = "application/json")
    public boolean deleteDataCategory(@PathVariable Integer id) {
        try {
            return categoryService.deleteCategory(id);
        } catch (Exception e) {
            log4j.error("CAN NOT DELETE DATA CATEGORY: " + e.getMessage());
            return false;
        }
    }

    @PostMapping(value = ValueAPI.CATEGORY_API_CHECK_NAME, produces = "application/json")
    public boolean checkNameCategory(String name) {
        try {
            return categoryService.checkNameCategory(name);
        } catch (Exception e) {
            log4j.error("CAN NOT FIND NAME: " + e.getMessage());
            return false;
        }
    }

}
