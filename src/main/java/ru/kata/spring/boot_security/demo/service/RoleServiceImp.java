package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;
@Service
public class RoleServiceImp implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> listRoles(String username) {
        return roleRepository.findAll(username);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional
    public void saveRole(Role role) {
        roleRepository.saveNew(role);
    }
}
