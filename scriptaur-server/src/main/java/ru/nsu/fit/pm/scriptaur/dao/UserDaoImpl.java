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

    private Transaction getTransaction(Session session) {
        Transaction trans;
        try {
            trans = session.beginTransaction();
        } catch (Exception e) {
            trans = session.getTransaction();
        }
        return trans;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void addUser(User user) {
        Session session = null;
        Transaction trans = null;
        try {
            session = getSession();
            trans = getTransaction(session);

            session.persist(user);
        } finally {

            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }
        }
    }

    public void updateUser(User user) {
        Session session = null;
        Transaction trans = null;
        try {
            session = getSession();
            trans = getTransaction(session);

            session.merge(user);
        } finally {

            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        Session session = null;
        Transaction trans = null;
        List<User> userList;

        try {
            session = getSession();
            trans = getTransaction(session);

            userList = session.createQuery("from ru.nsu.fit.pm.scriptaur.entity.User").list();
            for (User user : userList) {
                System.out.println("dao " + user.getUsername());
            }
        } finally {

            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }
        }

        return userList;
    }

    @Override
    public User getUserByUsername(String username) {
        Session session = null;
        Transaction trans = null;
        User user;
        try {
            session = getSession();
            trans = getTransaction(session);


            SQLQuery sqlQuery = session.createSQLQuery("SELECT * from users WHERE username = '" + username + "'")
                    .addEntity(User.class);

            List list = sqlQuery.list();

            if (1 != list.size()) {
                return null;
            }
            user = (User) list.get(0);
        } catch (Exception e) {
            return null;
        } finally {
            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }

        }
        return user;

    }

    public User getUserById(int id) {
        Session session = null;
        Transaction trans = null;
        User user;
        try {
            session = getSession();
            trans = getTransaction(session);


            SQLQuery sqlQuery = session.createSQLQuery("SELECT * from users WHERE user_id = '" + id + "'")
                    .addEntity(User.class);

            List list = sqlQuery.list();

            if (1 != list.size()) {
                return null;
            }
            user = (User) list.get(0);
        } catch (Exception e) {
            return null;
        } finally {
            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }

        }
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
        Session session = null;
        Transaction trans = null;
        try {
            session = getSession();
            trans = getTransaction(session);

            User user = session.get(User.class, userId);
            user.setTrustFactor(trustFactor);
            session.merge(user);
        } finally {
            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }

        }
    }

    @Override
    public void updateTrustFactorDay(int userId) {
        Session session = null;
        Transaction trans = null;
        try {
            session = getSession();
            trans = getTransaction(session);

            User user = session.get(User.class, userId);
            user.setTrustFactorUpdated(new Date());
            session.merge(user);
        } finally {
            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }

        }
    }

    @Override
    public float getUserTrustFactor(int userIdByToken) {
        Session session = null;
        Transaction trans = null;
        User user;
        try {
            session = getSession();
            trans = getTransaction(session);

            Criteria cr = session.createCriteria(User.class)
                    .add(Restrictions.gt("userId", userIdByToken));

            user = (User) cr.list().get(0);
        } catch (Exception e) {
            return -1;
        } finally {
            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }

        }

        return user.getTrustFactor();
    }

    @Override
    public Date getDateOfTrustFactorUpdated(int userId) {
        Session session = null;
        Transaction trans = null;
        Date date;
        try {
            session = getSession();
            trans = getTransaction(session);

            Criteria cr = session.createCriteria(User.class).add(Restrictions.eq("userId", userId));
            List list = cr.list();


            if (1 != list.size()) {
                return null;
            }
            User user = (User) list.get(0);
            date = user.getTrustFactorUpdated();
        } finally {
            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }

        }

        return date;
    }
}
