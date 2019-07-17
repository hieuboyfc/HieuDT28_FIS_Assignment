package hieuboy.developer.services.impls;

import hieuboy.developer.entities.Permission;
import hieuboy.developer.entities.Role;
import hieuboy.developer.entities.User;
import hieuboy.developer.models.UserDetailsModel;
import hieuboy.developer.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null)
                throw new UsernameNotFoundException("Không đúng tên đăng nhập hoặc mật khẩu!");
            else {
                Set<Role> lstRole = user.getRoles();
                for (Role data : lstRole) {
                    if (data.getId() == 3 || data.getName().equalsIgnoreCase("Thành viên"))
                        throw new UsernameNotFoundException("Tài khoản không có quyền truy cập hệ thống!");
                }
            }
            return new UserDetailsModel(user.getId(), username, user.getPassword(), !user.getIslock(),
                    true, true, true,
                    getAuthorities(user.getRoles()), user.getFullname(),
                    user.getAvatar());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> permission = new ArrayList<>();
        final List<Permission> collection = new ArrayList<>();
        for (final Role role : roles) {
            collection.addAll(role.getPermissions());
        }
        for (final Permission item : collection) {
            permission.add(item.getLink());
        }
        return permission;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> permission) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> lstPermission = new ArrayList<>();
        for (final String privilege : permission) {
            lstPermission.add(privilege);
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        System.out.println("Danh sach permission user: " + lstPermission);
        return authorities;
    }

}