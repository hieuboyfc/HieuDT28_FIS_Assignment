package hieuboy.developer.services;

import hieuboy.developer.models.GroupPermissionModel;

import java.util.List;

public interface IGroupPermissionService {
    List<GroupPermissionModel> getListGroupPermission(Integer parentId);

    GroupPermissionModel getDetailGroupPermission(Integer id);

    boolean saveGroupPermission(GroupPermissionModel groupPermissionModel);

    boolean updateGroupPermission(GroupPermissionModel groupPermissionModel);

    boolean deleteGroupPermission(Integer id);

    String getPermissionByGroupAndConvertToJSONString(Integer parentNode, List<Integer> permission);
}
