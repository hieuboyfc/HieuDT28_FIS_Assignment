package hieuboy.developer.mappers;

import hieuboy.developer.entities.Permission;
import hieuboy.developer.models.PermissionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionMapper {
    PermissionMapper MAPPER = Mappers.getMapper(PermissionMapper.class);

    @Mappings({
            @Mapping(target = "groupID", source = "groupPermission.id"),
    })
    PermissionModel mapEntityToModel(Permission permission);

    @Mappings({
            @Mapping(target = "groupPermission", ignore = true),
    })
    Permission mapModelToEntity(PermissionModel permissionModel);

    @Mappings({
            @Mapping(target = "groupPermission", ignore = true),
            @Mapping(target = "islock", ignore = true),
    })
    void updateFromDTOToEntity(PermissionModel permissionModel, @MappingTarget Permission permission);

}
