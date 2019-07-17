package hieuboy.developer.mappers;

import hieuboy.developer.entities.Role;
import hieuboy.developer.entities.User;
import hieuboy.developer.models.UserModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "pageIndex", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "column", ignore = true),
            @Mapping(target = "desending", ignore = true),
            @Mapping(target = "lstRole", source = "user", qualifiedByName = "lstRole"),
    })
    UserModel mapEntityToModel(User user);

    List<UserModel> mapListEntityToListModel(Stream<User> userStream);

    @Mappings({
            @Mapping(target = "roles", ignore = true),
    })
    User mapModelToEntity(UserModel userModel);

    @Mappings({
            @Mapping(target = "datecreated", ignore = true),
            @Mapping(target = "usercreated", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "roles", ignore = true),
    })
    void updateFromDTOToEntity(UserModel userModel, @MappingTarget User user);

    @Mappings({
            @Mapping(target = "datecreated", ignore = true),
            @Mapping(target = "usercreated", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "islock", ignore = true),
            @Mapping(target = "lockreason", ignore = true),
            @Mapping(target = "gender", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "username", ignore = true),
    })
    void changeProfileFromDTOToEntity(UserModel userModel, @MappingTarget User user);

    @Named("lstRole")
    default List<Integer> lstRole(User user) {
        if (user.getRoles() != null)
            return user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
