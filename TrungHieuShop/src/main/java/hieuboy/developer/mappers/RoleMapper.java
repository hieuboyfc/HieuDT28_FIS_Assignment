package hieuboy.developer.mappers;

import hieuboy.developer.entities.Permission;
import hieuboy.developer.entities.Role;
import hieuboy.developer.models.RoleModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper
public interface RoleMapper {

    RoleMapper MAPPER = Mappers.getMapper(RoleMapper.class);

    @Mappings({
            @Mapping(target = "pageIndex", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "column", ignore = true),
            @Mapping(target = "desending", ignore = true),
            @Mapping(target = "lstPermission", source = "role", qualifiedByName = "lstPermission"),
    })
    RoleModel mapEntityToModel(Role role);

    List<RoleModel> mapListEntityToListModel(Stream<Role> roleStream);

    @Mappings({
            @Mapping(target = "permissions", ignore = true),
    })
    Role mapModelToEntity(RoleModel roleModel);

    @Mappings({
            @Mapping(target = "permissions", ignore = true),
    })
    void updateFromDTOToEntity(RoleModel roleModel, @MappingTarget Role role);

    @Named("lstPermission")
    default List<Integer> lstPermission(Role role) {
        if (role.getPermissions() != null)
            return role.getPermissions().stream().map(Permission::getId).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
