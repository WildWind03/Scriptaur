package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.pm.scriptaur.entity.Evaluation;

import java.util.List;


@SuppressWarnings("unchecked")
@Repository
public class EvaluationDaoImpl implements EvaluationDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addMark(int userId, int videoId, int mark) {
        Evaluation evaluation = new Evaluation(mark, userId, videoId);

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(evaluation);

        transaction.commit();
        session.close();
    }

    @Override
    public Evaluation getEvaluationByVideoId(int userId, int videoId) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();
        Evaluation evaluation = (Evaluation) session.load(Evaluation.class, new Evaluation.EvaluationId(videoId, userId));
        trans.commit();
        session.close();
        return evaluation;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Evaluation> getEvaluationList(int page) {
        Session session = getSession();

        Transaction trans = session.beginTransaction();
        List<Evaluation> evaluations = session.createQuery("from ru.nsu.fit.pm.scriptaur.entity.Evaluation").list();

        trans.commit();
        session.close();
        return evaluations;
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
