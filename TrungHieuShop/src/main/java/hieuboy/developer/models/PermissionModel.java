package hieuboy.developer.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String link;
    private Boolean islock;
    private Integer groupID;

}
