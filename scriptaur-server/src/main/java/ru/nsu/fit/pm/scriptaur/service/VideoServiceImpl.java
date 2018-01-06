package ru.nsu.fit.pm.scriptaur.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.pm.scriptaur.dao.VideoDao;
import ru.nsu.fit.pm.scriptaur.entity.Video;

import java.util.List;

@Service
@Transactional
@ComponentScan("ru.nsu.fit.scriptaur.dao")
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDao videoDao;

    @Override
    @Transactional
    public Video addVideo(Video v) {
        return this.videoDao.addVideo(v);
    }

    @Override
    @Transactional
    public List<Video> getVideoList(int page) {
        return this.videoDao.getVideoList(page);
    }

    @Override
    @Transactional
    public void removeVideo(int id) {
        this.videoDao.removeVideo(id);
    }

    @Override
    public Video getVideoById(int id) {
        return this.videoDao.getVideoById(id);
    }

    @Override
    public Video updateVideoRating(int id, float rating) {
        return this.videoDao.updateVideoRating(id, rating);
    }

    @Override
    public int getEvaluationCount(int id) {
        return this.videoDao.getEvaluationCount(id);
    }

    @Override
    public void updateEvaluationsCount(int videoId) {
        this.videoDao.updateEvaluationCount(videoId);
    }

    @Override
    public List<Video> getVideoListLastMonthByUserId(int userId) {
        return this.videoDao.getVideoListByUserId(userId);
    }

    @Override
    public List<Video> getVideoListLastMonthByUserId(int userId, int page) {
        return videoDao.getVideoListByUserId(userId, page);
    }

    @Override
    public List<Video> getAllVideosByPage(int page) {
        return videoDao.getAllVideosByPage(page);
    }

    @Override
    public int getCountOfPagesVideos() {
        return videoDao.getCountOfPagesVideo();
    }

    @Override
    public int getCountOfPagesVideosByUserId(int user_id) {
        return videoDao.getCountOfPagesVideosByUserId(user_id);
    }

    @Override
    public int getCountOfPagesVideosByQuery(String query) {
        return videoDao.getCountOfPagesVideosByQuery(query);
    }

    @Override
    public List<Video> findVideoList(int page, String query) {
        return videoDao.findVideoList(page, query);
    }
}
