package hieuboy.developer.services;

import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.CategoryModel;

import java.util.List;

public interface ICategoryService {

    List<CategoryModel> getAllCategory();

    AjaxResultPagingModel getListCategoryModel(CategoryModel model);

    boolean saveCategory(CategoryModel model);

    int updateCategory(CategoryModel model);

    boolean deleteCategory(Integer id);

    boolean checkNameCategory(String name);

    CategoryModel getDetailCategory(Integer id);
}
