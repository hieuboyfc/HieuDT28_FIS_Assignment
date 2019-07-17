package hieuboy.developer.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Data
public class ProductModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String code;
    private String name;
    private Double price;
    private Integer quantity;
    private String imageLink;
    private String imageList;
    private Integer discount;
    private String content;
    private Integer view;
    private Integer status;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date datecreated;
    private String usercreated;

    private Integer categoryID;
    private String categoryName;
    private Integer manufacturerID;
    private String manufacturerName;

    private Integer pageIndex;
    private Integer pageSize;
    private String column;
    private Integer desending;
}
