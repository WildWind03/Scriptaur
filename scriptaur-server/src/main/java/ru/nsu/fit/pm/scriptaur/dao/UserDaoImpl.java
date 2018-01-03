package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.pm.scriptaur.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {


    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        Session session = null;

        try {

            sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        return session;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void addUser(User user) {
        Session session = getSession();

        Transaction trans = session.beginTransaction();
        session.persist(user);

        trans.commit();
    }

    public void updateUser(User user) {
        Session session = getSession();
        session.update(user);
    }

    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        Session session = getSession();

        Transaction trans = session.beginTransaction();
        List<User> userList = session.createQuery("from ru.nsu.fit.pm.scriptaur.entity.User").list();
        for (User user : userList) {
            System.out.println("dao " + user.getUsername());
        }

        trans.commit();
        return userList;
    }

    public User getUserById(int id) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();
        User user = (User) session.load(User.class, id);
        System.out.println(user.getUsername());
        trans.commit();
        return user;
    }

    public void removeUser(int id) {
        Session session = getSession();

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
