package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Repository
public class RoleRepositoryImp implements RoleRepository {
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;
    @Override
    public Role findByName(String name) {
        Query q = entityManager.createNativeQuery("select * from Roles where name = :name", Role.class);
        q.setParameter("name", name);
        return (Role)q.getSingleResult();
    }
    @Override
    public List<Role> findAll(String username) {
        Query q = entityManager.createNativeQuery(
                "select r.id, r.name from roles r " +
                        "INNER JOIN users_roles ur ON ur.role_id = r.id " +
                        "INNER JOIN users u ON u.id = ur.user_id " +
                        "where u.UserName = :username", Role.class);
        q.setParameter("username", username);
        return (List<Role>) q.getResultList();
    }

    @Override
    public List<Role> listOfRoles() {
        Query q = entityManager.createNativeQuery("select * from Roles", Role.class);
        return (List<Role>) q.getResultList();
    }

    @Override
    public void saveNew(Role role) {
        entityManager.persist(role);
    }
}
