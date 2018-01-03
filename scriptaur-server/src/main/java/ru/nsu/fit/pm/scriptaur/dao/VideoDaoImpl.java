package ru.nsu.fit.pm.scriptaur.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.entity.Video;

import java.util.List;

@Repository
public class VideoDaoImpl implements VideoDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoDaoImpl.class.getName());

    private final SessionFactory sessionFactory;

    @Autowired
    public VideoDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Video addVideo(Video video) {
        Session session = sessionFactory.getCurrentSession();

        Transaction transaction = session.beginTransaction();
        session.persist(video);

        transaction.commit();
        return video;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Video> getVideoList(int page) {
        Session session = sessionFactory.getCurrentSession();

        Transaction trans = session.beginTransaction();
        List<Video> videoList = session.createQuery("from ru.nsu.fit.pm.scriptaur.entity.Video").list();
        trans.commit();

        return videoList;
    }

    @Override
    public void removeVideo(int id) {
        Session session = sessionFactory.getCurrentSession();

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
}
