package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    public UserServiceImp(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }
    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
    public User get(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void save(User user) {
        User tempUser = findByUsername(user.getUsername());
        tempUser.setId(user.getId());
        tempUser.setUsername(user.getUsername());
        tempUser.setEmail(user.getEmail());
        tempUser.setAge(user.getAge());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        tempUser.setPassword(encodedPassword);
        tempUser.setRoles(user.getRoles());
        userRepository.save(tempUser);
    }
    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
    @Transactional
    public void registerDefaultUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB == null) {
            Role roleUser = roleRepository.findByName("ROLE_USER");
            user.addRole(roleUser);
            encodePassword(user);
            userRepository.saveNew(user);
        }
    }
    @Transactional
    public void saveNew(User user) {
        encodePassword(user);
        userRepository.saveNew(user);
    }
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    public List<User> showUsers() {
        return userRepository.findAll();
    }
    public List<Role> listOfRoles(User user) {
        return roleRepository.findAll(user.getUsername());
    }
}
