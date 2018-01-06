package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.*;
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
        Session session = getSession();
        Transaction trans = session.beginTransaction();
        TokenIdMather tokenIdMather = session.get(TokenIdMather.class, token);

        if (null == tokenIdMather) {
            throw new NoEntityException("No id matching to the token");
        }

        trans.commit();
        session.close();
        return tokenIdMather.getId();
    }

    @Override
    public void addNew(int id, String token) {
        TokenIdMather tokenIdMatherDao = new TokenIdMather(token, id);
        Session session = getSession();

        Transaction trans = session.beginTransaction();
        session.persist(tokenIdMatherDao);

        trans.commit();
        session.close();
    }

    @Override
    public void delete(String token) {
        Session session = getSession();

        Transaction trans = session.beginTransaction();
        TokenIdMather tokenIdMather = session.load(TokenIdMather.class, token);
        if (tokenIdMather != null) {
            session.delete(tokenIdMather);
        } else {
            LOGGER.error("Token Id Mather doesn't exist");
        }

        trans.commit();
        session.close();
    }

    @Override
    public void delete(int userId) {
        Session session = getSession();
        Transaction trans;
        try {
            trans = session.beginTransaction();
        } catch (Exception e) {
            trans = session.getTransaction();
        }

        SQLQuery sqlQuery = session.createSQLQuery("SELECT * from token_id_mather WHERE id = '" + userId + "'")
                .addEntity(TokenIdMather.class);

        List list = sqlQuery.list();
        list.forEach(session::delete);
//        Criteria cr = session.createCriteria(User.class)
//                .add(Restrictions.gt("userId", id));
//
//        User user = (User) cr.list().get(0);

        trans.commit();
        session.close();
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
}
