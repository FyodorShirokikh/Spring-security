package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserRepository {

    User findByUsername(String username);

    User findById(Long id);

    void save(User user);

    void deleteById(Long id);

    List<User> findAll();

    void saveNew(User user);
}
