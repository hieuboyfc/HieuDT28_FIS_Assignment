package hieuboy.developer.utils;

import hieuboy.developer.entities.User;
import hieuboy.developer.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationHelper {
    private static final Logger log4j = LoggerFactory.getLogger(AuthenticationHelper.class);

    private UserRepository userRepository;

    public AuthenticationHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUsernameLoginInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return userRepository.findByUsername(authentication.getName());
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return null;
        }
    }
}
