package hieuboy.developer.models;

import hieuboy.developer.entities.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public class GroupPermissionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private Integer parentID;
    private List<Permission> lstPermissions;
    private List<GroupPermissionModel> lstGroupPermissionModel;
}
