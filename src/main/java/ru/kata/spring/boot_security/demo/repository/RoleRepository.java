package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;


public interface RoleRepository {
    Role findByName(String name);

    List<Role> findAll(String username);

    List<Role> listOfRoles();

    void saveNew(Role role);

}
