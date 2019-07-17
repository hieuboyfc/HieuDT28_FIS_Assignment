package hieuboy.developer.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Table(name = "Group_Permission")
@Entity
@Data
public class GroupPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "parent_id")
    private Integer parentID;

    @OneToMany(mappedBy = "groupPermission", cascade = CascadeType.REMOVE)
    private List<Permission> permissions;

}
