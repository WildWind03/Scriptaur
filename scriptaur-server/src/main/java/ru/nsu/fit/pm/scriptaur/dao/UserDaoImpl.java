package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.pm.scriptaur.entity.User;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addUser(User user) {
        Session session = sessionFactory.getCurrentSession();

        Transaction trans = session.beginTransaction();
        session.persist(user);

        trans.commit();
    }

    @Override
    public void updateUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();

        Transaction trans = session.beginTransaction();
        List<User> userList = session.createQuery("from ru.nsu.fit.pm.scriptaur.entity.User").list();
        for (User user : userList) {
            System.out.println("dao " + user.getUsername());
        }

        trans.commit();
        return userList;
    }

    @Override
    public User getUserById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        User user = (User) session.load(User.class, id);
        System.out.println(user.getUsername());
        trans.commit();
        return user;
    }

    @Override
    public void removeUser(int id) {
        Session session = sessionFactory.getCurrentSession();

        Transaction trans = session.beginTransaction();
        User user = (User) session.load(User.class, id);
        if (user != null) {
            System.out.println(user.getUsername());
            session.delete(user);
        } else {
            System.out.println("nothing");
        }

        trans.commit();
    }
}
