package hieuboy.developer.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "Product")
@Entity
@Data
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private String name;
    private Double price;
    private Integer quantity;
    @Column(name = "image_link")
    private String imageLink;
    @Column(name = "image_list")
    private String imageList;
    private Integer discount;
    private String content;
    private Integer view;
    private Integer status;
    private Date datecreated;
    private String usercreated;

    @Column(name = "category_id")
    private Integer categoryID;

    @Column(name = "manufacturer_id")
    private Integer manufacturerID;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", insertable = false, updatable = false)
    private Manufacturer manufacturer;


}
