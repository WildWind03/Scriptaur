package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.fit.pm.scriptaur.entity.TokenIdMather;
import ru.nsu.fit.pm.scriptaur.entity.User;

import java.util.List;
import java.util.function.Consumer;

@Component
public class TokenIdMatherDaoImpl implements TokenIdMatherDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenIdMather.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int getId(String token) throws NoEntityException {
        Session session = null;
        Transaction trans = null;
        int id;
        try {
            session = getSession();
            trans = getTransaction(session);

            TokenIdMather tokenIdMather = session.get(TokenIdMather.class, token);

            if (null == tokenIdMather) {
                throw new NoEntityException("No id matching to the token");
            }

            id = tokenIdMather.getId();


        } finally {
            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }
        }
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addNew(int id, String token) {

        Session session = null;
        Transaction trans = null;
        try {
            session = getSession();
            trans = getTransaction(session);

            TokenIdMather tokenIdMather = new TokenIdMather(token, id);

            Criteria cr = session.createCriteria(TokenIdMather.class)
                    .add(Restrictions.eq("id", tokenIdMather.getId()));

            List<TokenIdMather> result = cr.list();

            if (result.size() == 0) {
                session.persist(tokenIdMather);
            } else {
                TokenIdMather old = result.get(0);
                old.setToken(token);
                session.merge(old);
            }
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
    public void delete(String token) {

        Session session = null;
        Transaction trans = null;
        try {
            session = getSession();
            trans = getTransaction(session);

            TokenIdMather tokenIdMather = session.load(TokenIdMather.class, token);
            if (tokenIdMather != null) {
                session.delete(tokenIdMather);
            } else {
                LOGGER.error("Token Id Mather doesn't exist");
            }
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
    @Override
    public void delete(int userId) {
        Session session = null;
        Transaction trans = null;
        try {
            session = getSession();
            trans = getTransaction(session);

            SQLQuery sqlQuery = session.createSQLQuery("SELECT * from token_id_mather WHERE id = '" + userId + "'")
                    .addEntity(TokenIdMather.class);

            List list = sqlQuery.list();
            list.forEach(session::delete);
        } finally {
            if (trans != null) {
                trans.commit();
            }
            if (session != null) {
                session.close();
            }

        }

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
}
