package hieuboy.developer.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.*;

@Data
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String fullname;
    private String gender;
    private String address;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthday;
    private String avatar;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date datecreated;
    private String usercreated;
    private Boolean islock;
    private String lockreason;

    List<Integer> lstRole;

    private Integer pageIndex;
    private Integer pageSize;
    private String column;
    private Integer desending;
}
