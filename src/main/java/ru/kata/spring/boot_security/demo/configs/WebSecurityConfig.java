package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.repository.RoleRepositoryImp;
import ru.kata.spring.boot_security.demo.repository.UserRepositoryImp;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final AccessDeniedUserHandler accessDeniedUserHandler;
    public WebSecurityConfig(SuccessUserHandler successUserHandler, AccessDeniedUserHandler accessDeniedUserHandler) {
        this.successUserHandler = successUserHandler;
        this.accessDeniedUserHandler = accessDeniedUserHandler;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("USER","ADMIN")
                .antMatchers("/", "/index", "/registration").permitAll()
                .and()
                .formLogin().successHandler(successUserHandler)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedUserHandler)
                .and()
                .logout()
                .permitAll().logoutSuccessUrl("/").permitAll();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImp(new BCryptPasswordEncoder(), new UserRepositoryImp(), new RoleRepositoryImp());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }
}