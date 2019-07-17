package hieuboy.developer.services;

import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.RoleModel;

import java.util.List;

public interface IRoleService {

    List<RoleModel> lstRole();

    AjaxResultPagingModel getListRoleModel(RoleModel roleModel);

    RoleModel getDetailRole(Integer id);

    boolean saveRole(RoleModel roleModel);

    boolean updateRolePermission(Integer roleId, Integer permissionId, boolean value);

    int updateNameRole(RoleModel roleModel);

    boolean deleteRole(List<Integer> ids);
}
