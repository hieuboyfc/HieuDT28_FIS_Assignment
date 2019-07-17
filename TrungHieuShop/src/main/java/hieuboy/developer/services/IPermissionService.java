package hieuboy.developer.services;

import hieuboy.developer.models.PermissionModel;

import java.util.List;

public interface IPermissionService {

    PermissionModel getDetailPermission(Integer id);

    List<PermissionModel> getPermissionByLink(String link);

    boolean savePermission(PermissionModel permissionModel);

    boolean updatePermission(PermissionModel permissionModel);

    boolean deletePermission(Integer id);

    boolean lockOrUnlockPermission(Integer id);

    boolean checkLink(String link);
}
