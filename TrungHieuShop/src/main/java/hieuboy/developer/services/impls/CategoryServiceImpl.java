package hieuboy.developer.services.impls;

import hieuboy.developer.entities.Category;
import hieuboy.developer.entities.User;
import hieuboy.developer.mappers.CategoryMapper;
import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.CategoryModel;
import hieuboy.developer.repositories.CategoryRepository;
import hieuboy.developer.repositories.UserRepository;
import hieuboy.developer.services.ICategoryService;
import hieuboy.developer.utils.AuthenticationHelper;
import hieuboy.developer.utils.HelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private static final Logger log4j = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private CategoryRepository categoryRepository;

    private UserRepository userRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CategoryModel> getAllCategory() {
        return CategoryMapper.MAPPER.mapListEntityToListModel(categoryRepository.findAll().stream());
    }

    @Override
    public AjaxResultPagingModel getListCategoryModel(CategoryModel model) {
        Sort sort = new Sort(model.getDesending() == 1 ? Sort.Direction.DESC : Sort.Direction.ASC, model.getColumn());
        Pageable pageable = PageRequest.of(model.getPageIndex(), model.getPageSize(), sort);
        Page<Category> lstCategory = categoryRepository.getListCategoryAndSearch(model.getName(), pageable);
        return new HelperService().pagingModel(model.getColumn(), model.getDesending(), model.getPageIndex(),
                model.getPageSize(), lstCategory.getTotalPages(), lstCategory.getTotalElements(),
                CategoryMapper.MAPPER.mapListEntityToListModel(lstCategory.getContent().stream()));
    }

    @Override
    @Transactional
    public boolean saveCategory(CategoryModel model) {
        try {
            Calendar calendar = Calendar.getInstance();
            Category category = CategoryMapper.MAPPER.mapModelToEntity(model);
            saveData(category, calendar);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public int updateCategory(CategoryModel model) {
        try {
            Category category = categoryRepository.findByCategoryId(model.getId());
            if (category != null) {
                CategoryMapper.MAPPER.updateFromDTOToEntity(model, category);
                categoryRepository.save(category);
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
    public boolean deleteCategory(Integer id) {
        try {
            categoryRepository.deleteByCategoryId(id);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkNameCategory(String name) {
        return categoryRepository.findByName(name) == null;
    }

    @Override
    public CategoryModel getDetailCategory(Integer id) {
        try {
            Category category = categoryRepository.findByCategoryId(id);
            if (category != null)
                return CategoryMapper.MAPPER.mapEntityToModel(category);
        } catch (Exception e) {
            log4j.error(e.getMessage());
        }
        return null;
    }

    private void saveData(Category category, Calendar calendar) {
        User userLogin = new AuthenticationHelper(userRepository).getUsernameLoginInfo();
        category.setUsercreated(userLogin.getUsername());
        category.setDatecreated(calendar.getTime());
        categoryRepository.save(category);
    }

}
