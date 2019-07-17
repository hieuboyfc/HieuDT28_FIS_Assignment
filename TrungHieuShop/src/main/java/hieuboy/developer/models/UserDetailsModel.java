package hieuboy.developer.models;

import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetailsModel extends User {
    private final Integer id;
    private final String fullname;
    private final String avatar;

    public UserDetailsModel(Integer id, String username, String password, boolean enabled,
                            boolean accountNonExpired, boolean credentialsNonExpired,
                            boolean accountNonLocked,
                            Collection authorities,
                            String fullname, String avatar) {

        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);

        this.id = id;
        this.fullname = fullname;
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAvatar() {
        return avatar;
    }
}
