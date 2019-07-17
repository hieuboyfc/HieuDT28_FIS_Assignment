package hieuboy.developer.mappers;

import hieuboy.developer.entities.GroupPermission;
import hieuboy.developer.models.GroupPermissionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupPermissionMapper {
    GroupPermissionMapper MAPPER = Mappers.getMapper(GroupPermissionMapper.class);

    @Mappings({
            @Mapping(target = "lstPermissions", ignore = true),
            @Mapping(target = "lstGroupPermissionModel", ignore = true)
    })
    GroupPermissionModel mapEntityToModel(GroupPermission groupPermission);

    @Mappings({
            @Mapping(target = "permissions", ignore = true),
    })
    GroupPermission mapModelToEntity(GroupPermissionModel groupPermissionModel);

    @Mappings({
            @Mapping(target = "permissions", ignore = true),
    })
    void updateFromDTOToEntity(GroupPermissionModel groupPermissionModel, @MappingTarget GroupPermission groupPermission);
}
