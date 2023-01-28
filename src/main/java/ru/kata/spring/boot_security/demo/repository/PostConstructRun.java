package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.annotation.PostConstruct;

@Repository
public class PostConstructRun {

    private final RoleService roleService;

    private final UserService userService;

    public PostConstructRun(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostConstruct
    public void postConstruct() {

        Role role1 = new Role("ROLE_USER");
        Role role2 = new Role("ROLE_ADMIN");

        roleService.saveRole(role1);
        roleService.saveRole(role2);

        User user1 = new User("Admin", "admin@gmail.com", 36, "12345");
        user1.addRole(roleService.findByName("ROLE_USER"));
        user1.addRole(roleService.findByName("ROLE_ADMIN"));

        User user2 = new User("Jane", "jane@gmail.com", 20, "123");
        user2.addRole(roleService.findByName("ROLE_USER"));

        userService.saveNew(user1);
        userService.saveNew(user2);
    }

}
