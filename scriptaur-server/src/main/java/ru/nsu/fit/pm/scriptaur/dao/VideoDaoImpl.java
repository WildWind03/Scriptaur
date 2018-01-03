package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.fit.pm.scriptaur.entity.Video;

import java.util.List;


@Component
public class VideoDaoImpl implements VideoDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoDaoImpl.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        Session session = null;

        try {

            sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        return session;
    }

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

    }

    @Override
    public Video getVideoById(int id) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();
        Video video = (Video) session.load(Video.class, id);
        System.out.println(video);
        trans.commit();
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

        return video;
    }

    @Override
    public int getEvaluationCount(int id) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();

        int count = session.get(Video.class, id).getEvaluationsCount();

        trans.commit();
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
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Video> getVideoListByUserId(int userId) {
        Session session = getSession();
        Transaction trans = session.beginTransaction();

        Query query = session.createQuery("from ru.nsu.fit.pm.scriptaur.entity.Video where added_by= :userId ");
        query.setParameter("userId", userId);
        List<Video> videos = query.list();

        trans.commit();
        System.out.println("video" + trans.getStatus());

        return videos;
    }
}
