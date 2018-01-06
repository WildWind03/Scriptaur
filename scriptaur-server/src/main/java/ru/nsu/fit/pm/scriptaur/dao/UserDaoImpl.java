package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.fit.pm.scriptaur.entity.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        session.close();
    }

    public void updateUser(User user) {
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(user);
        transaction.commit();
        session.close();
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
        session.close();
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
        session.close();
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

        SQLQuery sqlQuery = session.createSQLQuery("SELECT * from users WHERE user_id = '" + id + "'")
                .addEntity(User.class);

        List list = sqlQuery.list();

        if (1 != list.size()) {
            return null;
        }
        User user = (User) list.get(0);
//        Criteria cr = session.createCriteria(User.class)
//                .add(Restrictions.gt("userId", id));
//
//        User user = (User) cr.list().get(0);

        trans.commit();
        session.close();
        return user;
    }

    public void removeUser(int id) {
        /*Session session = getSession();

        Transaction trans = session.beginTransaction();
        User user = (User) session.load(User.class, id);
        if (user != null) {
            System.out.println(user.getUsername());
            session.delete(user);
        } else {
            System.out.println("nothing");
        }

        trans.commit();
        session.close();*/
    }

    @Override
    public void updateTtustFactor(int userId, float trustFactor) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();


        User user = session.get(User.class, userId);
        user.setTrustFactor(trustFactor);
        session.merge(user);

        trans.commit();
        session.close();
    }

    @Override
    public void updateTrustFactorDay(int userId) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();


        User user = session.get(User.class, userId);
        user.setTrustFactorUpdated(new Date());
        session.merge(user);

        trans.commit();
        session.close();
    }

    @Override
    public float getUserTrustFactor(int userIdByToken) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();

        Criteria cr = session.createCriteria(User.class)
                .add(Restrictions.gt("userId", userIdByToken));

        User user = (User) cr.list().get(0);

        tr.commit();
        session.close();

        return user.getTrustFactor();
    }

    @Override
    public Date getDateOfTrustFactorUpdated(int userId) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();


        /*SQLQuery sqlQuery = session.createSQLQuery("SELECT trust_factor_updated from users WHERE user_id =" + userId)
                .addEntity(User.class);
        List list = sqlQuery.list();
*/
        Criteria cr = session.createCriteria(User.class).add(Restrictions.eq("userId", userId));
        List list = cr.list();


        if (1 != list.size()) {
            return null;
        }
        User user = (User) list.get(0);
        Date date = user.getTrustFactorUpdated();

        tr.commit();
        session.close();

        return date;
    }
}
