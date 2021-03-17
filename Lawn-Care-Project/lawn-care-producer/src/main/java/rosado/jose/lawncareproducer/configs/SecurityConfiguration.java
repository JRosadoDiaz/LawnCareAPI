package rosado.jose.lawncareproducer.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import rosado.jose.lawncareproducer.model.User;
import rosado.jose.lawncareproducer.repositories.UserJpaRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static String AUTH_USER = "USER";
    public static String AUTH_CONTRACTOR = "CONTRACTOR";
    public static String AUTH_ADMIN = "ADMIN";

    private Map<String, User> users = new HashMap<>();

    @Autowired
    private UserJpaRepository repo;

    @PostConstruct
    private void initUsers() {
        List<User> userList = repo.findAll();
        userList.forEach(e -> users.put(e.getUsername(), e));
        userList.forEach(e -> e.setAuthorities(Arrays.asList(e.getAuthorityString())));
        User adminUser = new User("admin", passwordEncoder().encode("admin"), "", "", true,AUTH_ADMIN, AUTH_USER, AUTH_CONTRACTOR);

        users.put(adminUser.getUsername(), adminUser);
    }

    public void addNewUser(User newUser) {
        users.put(newUser.getUsername(), newUser);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                return users.get(s);
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
