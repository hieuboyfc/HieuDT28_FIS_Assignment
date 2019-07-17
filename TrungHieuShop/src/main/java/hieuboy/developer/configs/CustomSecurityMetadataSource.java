package hieuboy.developer.configs;

import hieuboy.developer.models.PermissionModel;
import hieuboy.developer.services.IPermissionService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;

public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private Map<String, List<ConfigAttribute>> resourceMap;
    private PathMatcher pathMatcher = new AntPathMatcher();
    private IPermissionService permissionService;

    CustomSecurityMetadataSource(IPermissionService permissionService) {
        super();
        this.permissionService = permissionService;
        resourceMap = null;
    }

    private Map<String, List<ConfigAttribute>> loadResourceMatchAuthority(String url) {
        List<PermissionModel> lstPermission = permissionService.getPermissionByLink(url);
        Map<String, List<ConfigAttribute>> map = new HashMap<>();
        if (lstPermission != null && !lstPermission.isEmpty()) {
            for (PermissionModel permission : lstPermission) {
                List<ConfigAttribute> list = new ArrayList<>();
                ConfigAttribute configAttribute = new SecurityConfig(permission.getLink());
                list.add(configAttribute);
                map.put(permission.getLink(), list);
            }
        }
        return map;
    }

    public List<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String url = ((FilterInvocation) object).getRequestUrl();
        resourceMap = loadResourceMatchAuthority(url);
        for (Map.Entry<String, List<ConfigAttribute>> requestUrl : resourceMap.entrySet()) {
            if (pathMatcher.match(requestUrl.getKey(), url)) {
                System.out.println("URL :" + "'" + url + "'");
                System.out.println("Danh sách permission URL: " + requestUrl.getValue());
                return requestUrl.getValue();
            }
        }
        return resourceMap.get(url);
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return Collections.emptyList();
    }

    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
