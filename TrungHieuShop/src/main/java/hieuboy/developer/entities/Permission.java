package hieuboy.developer.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "Permission")
@Entity
@Data
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String link;
    private boolean islock;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupPermission groupPermission;

}
