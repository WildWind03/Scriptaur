package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.query.spi.sql.NativeSQLQueryReturn;
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
        Transaction transaction = session.beginTransaction();
        session.merge(user);
        transaction.commit();

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
        System.out.println("user" + trans.getStatus());
        return userList;
    }

    @Override
    public User getUserByUsername(String username) {
        Session session = getSession();
        Transaction trans;
        try {
            trans = session.beginTransaction();
        } catch (Exception e) {
            trans = session.getTransaction();
        }

        SQLQuery sqlQuery = session.createSQLQuery("SELECT * from users WHERE username = '" + username + "'")
                .addEntity(User.class);

        List list = sqlQuery.list();

        if (1 != list.size()) {
            return null;
        }
        User user = (User) list.get(0);

        trans.commit();
        return user;
    }

    public User getUserById(int id) {
        Session session = getSession();
        Transaction trans;
        try {

        trans = session.beginTransaction();
        } catch (Exception e) {
            trans = session.getTransaction();
        }

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

    @Override
    public void updateTtustFactor(int userId, float trustFactor) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();

        User user = session.get(User.class, userId);
        user.setTrustFactor(trustFactor);
        session.merge(user);

        trans.commit();
    }
}