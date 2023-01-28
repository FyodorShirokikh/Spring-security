package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> listRoles(String username);
    Role findByName(String name);
    void saveRole(Role role);

}
