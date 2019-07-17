package hieuboy.developer.mappers;

import hieuboy.developer.entities.Category;
import hieuboy.developer.models.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

@Mapper
public interface CategoryMapper {
    CategoryMapper MAPPER = Mappers.getMapper(CategoryMapper.class);

    @Mappings({
            @Mapping(target = "pageIndex", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "column", ignore = true),
            @Mapping(target = "desending", ignore = true),
    })
    CategoryModel mapEntityToModel(Category category);

    List<CategoryModel> mapListEntityToListModel(Stream<Category> categoryStream);

    @Mappings({
            @Mapping(target = "products", ignore = true),
    })
    Category mapModelToEntity(CategoryModel categoryModel);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "datecreated", ignore = true),
            @Mapping(target = "usercreated", ignore = true),
            @Mapping(target = "products", ignore = true),
    })
    void updateFromDTOToEntity(CategoryModel categoryModel, @MappingTarget Category category);
}
