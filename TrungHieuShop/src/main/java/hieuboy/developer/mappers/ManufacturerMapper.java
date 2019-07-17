package hieuboy.developer.mappers;

import hieuboy.developer.entities.Manufacturer;
import hieuboy.developer.models.ManufacturerModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

@Mapper
public interface ManufacturerMapper {
    ManufacturerMapper MAPPER = Mappers.getMapper(ManufacturerMapper.class);

    @Mappings({
            @Mapping(target = "pageIndex", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "column", ignore = true),
            @Mapping(target = "desending", ignore = true),
    })
    ManufacturerModel mapEntityToModel(Manufacturer manufacturer);

    List<ManufacturerModel> mapListEntityToListModel(Stream<Manufacturer> manufacturerStream);

    @Mappings({
            @Mapping(target = "products", ignore = true),
    })
    Manufacturer mapModelToEntity(ManufacturerModel manufacturerModel);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "datecreated", ignore = true),
            @Mapping(target = "usercreated", ignore = true),
            @Mapping(target = "products", ignore = true),
    })
    void updateFromDTOToEntity(ManufacturerModel manufacturerModel, @MappingTarget Manufacturer manufacturer);
}
