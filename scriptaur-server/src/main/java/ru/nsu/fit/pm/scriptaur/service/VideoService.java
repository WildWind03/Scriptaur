package ru.nsu.fit.pm.scriptaur.service;

import ru.nsu.fit.pm.scriptaur.entity.Video;

import java.util.List;

public interface VideoService {

    Video addVideo(Video v);

    List<Video> getVideoList(int page);

    void removeVideo(int id);

    Video getVideoById(int id);

    Video updateVideoRating(int id, float rating);

    int getEvaluationCount(int id);

    void updateEvaluationsCount(int videoId);

    List<Video> getVideoListLastMonthByUserId(int userId);

    List<Video> getVideoListLastMonthByUserId(int userId, int page);

    List<Video> getAllVideosByPage(int page);

    int getCountOfPagesVideos();

    int getCountOfPagesVideosByUserId(int user_id);

    int getCountOfPagesVideosByQuery(String query);

    List<Video> findVideoList(int page, String query);

    int getAuthorId(int videoId);
}
