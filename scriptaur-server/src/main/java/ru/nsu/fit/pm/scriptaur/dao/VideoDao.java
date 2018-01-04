package ru.nsu.fit.pm.scriptaur.dao;

import ru.nsu.fit.pm.scriptaur.entity.Video;

import java.util.List;

public interface VideoDao {

    Video addVideo(Video v);

    List<Video> getVideoList(int page);

    void removeVideo(int id);

    Video getVideoById(int id);

    Video updateVideoRating(int id, float rating);


    int getEvaluationCount(int id);

    void updateEvaluationCount(int videoId);

    List<Video> getVideoListByUserId(int userId);
}