package hieuboy.developer.configs;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

public class CustomAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

        if (configAttributes == null) {
            return;
        }

        Iterator<ConfigAttribute> iterator = configAttributes.iterator();

        while(iterator.hasNext()) {
            ConfigAttribute configAttribute = iterator.next();
            String functionId = configAttribute.getAttribute();
            for(GrantedAuthority ga : authentication.getAuthorities()) {
                if(functionId.equals(ga.getAuthority())) {
                    return;
                }
            }
        }

        throw new AccessDeniedException("Không có quyền đăng nhập hệ thống!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
