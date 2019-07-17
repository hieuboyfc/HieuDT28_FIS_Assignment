package hieuboy.developer.models;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private List<Integer> lstPermission;

    private Integer pageIndex;
    private Integer pageSize;
    private String column;
    private Integer desending;
}
