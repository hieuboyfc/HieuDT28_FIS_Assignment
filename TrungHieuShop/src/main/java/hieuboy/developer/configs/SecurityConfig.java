package hieuboy.developer.configs;

import hieuboy.developer.services.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private DataSource dataSource;

    private UserDetailsService userDetailsService;

    private IPermissionService permissionService;

    public SecurityConfig(DataSource dataSource,
                          UserDetailsService userDetailsService,
                          IPermissionService permissionService) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
        this.permissionService = permissionService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/login").anonymous()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/notfound").anonymous()
                .antMatchers("/", "/admin/**", "/api/**").authenticated().withObjectPostProcessor(
                new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        FilterInvocationSecurityMetadataSource newSource =
                                new CustomSecurityMetadataSource(permissionService);
                        AccessDecisionManager accessDecisionManager = new CustomAccessDecisionManager();
                        fsi.setSecurityMetadataSource(newSource);
                        fsi.setAccessDecisionManager(accessDecisionManager);
                        return fsi;
                    }
                })
                .anyRequest().permitAll()
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/admin/home").usernameParameter("username").passwordParameter("password")
                .failureUrl("/login?error").successHandler(savedRequestAwareAuthenticationSuccessHandler())
                .and().logout().logoutSuccessUrl("/login?logout")
                .and().exceptionHandling().accessDeniedPage("/notfound")
                .and().rememberMe().rememberMeParameter("remember-me")
                .tokenRepository(persistentTokenRepository()).tokenValiditySeconds(86400)
                .and().csrf().ignoringAntMatchers("/api/**");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(dataSource);
        return tokenRepositoryImpl;
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler auth = new SavedRequestAwareAuthenticationSuccessHandler();
        auth.setTargetUrlParameter("href");
        return auth;
    }
}
