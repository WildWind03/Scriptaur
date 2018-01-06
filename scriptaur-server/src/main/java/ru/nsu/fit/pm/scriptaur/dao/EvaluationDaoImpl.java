package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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


    @Override
    public Integer getUserMarkByVideoId(int userId, int videoId) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();

        Criteria cr = session.createCriteria(Evaluation.class)
                .add(Restrictions.eq("EvaluationId.userId", userId))
                .add(Restrictions.eq("EvaluationId.videoId", videoId));

        //SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM evaluations WHERE user_id=" + userId + " AND video_id=" + videoId)
               /* .addEntity(Evaluation.class)*/;

        Query query = session.createQuery("from ru.nsu.fit.pm.scriptaur.entity.Evaluation where user_id = :userId and video_id = :videoId");
        query.setParameter("userId", userId);
        query.setParameter("videoId", videoId);

        List<Evaluation> list = query.list();


        tr.commit();
        session.close();

        Integer mark;
        try {
            Evaluation evaluation = list.get(0);
            mark = evaluation.getMark();
            //mark = (Object)(list.get(0))[3];
        } catch (Exception e) {
            return null;
        }
        return mark;
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
