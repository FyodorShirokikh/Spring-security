package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    UserDetails loadUserByUsername(String username);
    Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles);
    User get(Long id);
    void save(User user);
    void registerDefaultUser(User user);
    void delete(Long id);
    List<User> showUsers();
    List<Role> listOfRoles(User user);
    void saveNew(User user);
}
