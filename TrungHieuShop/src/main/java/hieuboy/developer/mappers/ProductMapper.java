package hieuboy.developer.mappers;

import hieuboy.developer.entities.Product;
import hieuboy.developer.models.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

@Mapper
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(target = "manufacturerID", source = "product.manufacturer.id"),
            @Mapping(target = "manufacturerName", source = "product.manufacturer.name"),
            @Mapping(target = "categoryID", source = "product.category.id"),
            @Mapping(target = "categoryName", source = "product.category.name"),
            @Mapping(target = "pageIndex", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "column", ignore = true),
            @Mapping(target = "desending", ignore = true),
    })
    ProductModel mapEntityToModel(Product product);

    List<ProductModel> mapListEntityToListModel(Stream<Product> productStream);

    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "manufacturer", ignore = true),
    })
    Product mapModelToEntity(ProductModel productModel);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "datecreated", ignore = true),
            @Mapping(target = "usercreated", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "manufacturer", ignore = true),
    })
    void updateFromDTOToEntity(ProductModel productModel, @MappingTarget Product product);
}
