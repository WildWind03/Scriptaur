package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.pm.scriptaur.entity.Evaluation;
import ru.nsu.fit.pm.scriptaur.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@SuppressWarnings("unchecked")
@Repository
public class EvaluationDaoImpl implements EvaluationDao {

//    @Autowired
    private SessionFactory sessionFactory;
    /*@PersistenceContext
    EntityManager entityManager;*/




    @Override
    public void addMark(int userId, int videoId, int mark) {
      /*  Evaluation evaluation = new Evaluation(mark, userId, videoId);

        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.persist(evaluation);

        transaction.commit();
*/    }

    @Override
    public Evaluation getEvaluationByVideoId(int userId, int videoId) {
  /*      Session session = sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Evaluation evaluation = (Evaluation) session.load(Evaluation.class, new Evaluation.EvaluationId(videoId, userId));
        trans.commit();
        return evaluation;
  */
  return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Evaluation> getEvaluationList(int page) {
        /*Session session = sessionFactory.getCurrentSession();

        Transaction trans = session.beginTransaction();
        List<Evaluation> evaluations = session.createQuery("from ru.nsu.fit.pm.scriptaur.entity.Evaluation").list();

        trans.commit();
        return evaluations;*/
        return null;
    }
}
