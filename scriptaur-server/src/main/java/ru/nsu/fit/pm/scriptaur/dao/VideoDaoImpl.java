package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.*;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.entity.Video;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


@Component
public class VideoDaoImpl implements VideoDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoDaoImpl.class.getName());
    public static int VIDEO_PRO_PAGE = 2;
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Video addVideo(Video video) {
        Session session = getSession();

        Transaction transaction = session.beginTransaction();
        session.persist(video);

        transaction.commit();
        return video;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Video> getVideoList(int page) {
        Session session = getSession();

        Transaction trans = session.beginTransaction();
        List<Video> videoList = session.createQuery("from ru.nsu.fit.pm.scriptaur.entity.Video").list();
        trans.commit();
        session.close();

        return videoList;
    }

    @Override
    public void removeVideo(int id) {
        Session session = getSession();

        Transaction trans = session.beginTransaction();
        Video video = (Video) session.load(Video.class, id);
        if (video != null) {
            session.delete(video);
            LOGGER.error("Video ");
        } else {
            LOGGER.error("Video doesn't exist");
        }

        trans.commit();
        session.close();

    }

    @Override
    public Video getVideoById(int id) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();
        Video video = (Video) session.load(Video.class, id);
        System.out.println(video);
        trans.commit();
        session.close();
        return video;
    }

    @Override
    public Video updateVideoRating(int id, float rating) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();

        Video video = session.get(Video.class, id);
        video.setRating(rating);
        session.merge(video);

        trans.commit();
        session.close();

        return video;
    }

    @Override
    public int getEvaluationCount(int id) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();

        int count = session.get(Video.class, id).getEvaluationsCount();

        trans.commit();
        session.close();
        return count;
    }

    @Override
    public void updateEvaluationCount(int videoId) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();

        Video video = session.get(Video.class, videoId);
        video.setEvaluationsCount(video.getEvaluationsCount() + 1);

        session.merge(video);

        trans.commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Video> getVideoListByUserId(int userId) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();


        Date today = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, -1);
        Date monthAgo = calendar.getTime();


        Criteria cr = session.createCriteria(Video.class)
                .add(Restrictions.eq("addedBy", userId))
                .add(Restrictions.between("addedOn", monthAgo, today));


        List<Video> videos = cr.list();
        for (Video video : videos) {
            System.out.println(video);
        }
        trans.commit();
        session.close();

        return videos;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Video> getVideoListByUserId(int userId, int page) {

        Session session = getSession();
        Transaction ts = session.beginTransaction();

        Criteria cr = session.createCriteria(Video.class)
                .add(Restrictions.eq("addedBy", userId))
                .setFirstResult(VIDEO_PRO_PAGE * page)
                .setMaxResults(VIDEO_PRO_PAGE);
        List<Video> videos = cr.list();


        ts.commit();
        session.close();


        return videos;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Video> getAllVideosByPage(int page) {

        Session session = getSession();
        Transaction tr = session.beginTransaction();

        Criteria cr = session.createCriteria(Video.class)
                .setFirstResult(VIDEO_PRO_PAGE * page)
                .setMaxResults(VIDEO_PRO_PAGE);
        List<Video> videos = cr.list();

        tr.commit();
        session.close();

        return videos;
    }

    @Override
    public int getCountOfPagesVideo() {
        Session session = getSession();
        Transaction tr = session.beginTransaction();

        Criteria cr = session.createCriteria(Video.class)
                .setProjection(Projections.rowCount());
        Long count = (Long) cr.uniqueResult();

        tr.commit();
        session.close();

        return (int) ((count + 1) / VIDEO_PRO_PAGE);
    }

    @Override
    public int getCountOfPagesVideosByUserId(int user_id) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();

        Criteria cr = session.createCriteria(Video.class)
                .add(Restrictions.eq("addedBy", user_id))
                .setProjection(Projections.rowCount());
        Long count = (Long) cr.uniqueResult();

        tr.commit();
        session.close();

        return (int) ((count + 1) / VIDEO_PRO_PAGE);
    }

    @Override
    public int getCountOfPagesVideosByQuery(String query) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();

        Criteria cr = session.createCriteria(Video.class)
                .add(Restrictions.like("name", query, MatchMode.ANYWHERE).ignoreCase())
                .setProjection(Projections.rowCount());
        Long count = (Long) cr.uniqueResult();

        tr.commit();
        session.close();

        return (int) ((count + 1) / VIDEO_PRO_PAGE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Video> findVideoList(int page, String query) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();

        Criteria cr = session.createCriteria(Video.class)
                .add(Restrictions.like("name", query, MatchMode.ANYWHERE).ignoreCase())
                .setFirstResult(VIDEO_PRO_PAGE * page)
                .setMaxResults(VIDEO_PRO_PAGE);

        List<Video> videos = cr.list();

        tr.commit();
        session.close();

        return videos;

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
