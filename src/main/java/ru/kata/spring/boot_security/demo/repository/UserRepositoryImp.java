package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Repository
public class UserRepositoryImp implements UserRepository {

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public User findByUsername(String username) {
        try {
            Query q = entityManager.createNativeQuery("select * from Users where username = :username", User.class);
            q.setParameter("username", username);
            return (User)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void save(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteById(Long id) {
        Query qr = entityManager.createNativeQuery("DELETE FROM users_roles WHERE user_id= :id");
        qr.setParameter("id", id);
        qr.executeUpdate();
        Query qu = entityManager.createNativeQuery("DELETE FROM users WHERE id= :id");
        qu.setParameter("id", id);
        qu.executeUpdate();
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("from User").getResultList();
    }

    @Override
    public void saveNew(User user) {
        entityManager.persist(user);
    }
}
