package ru.nsu.fit.pm.scriptaur.service;

import ru.nsu.fit.pm.scriptaur.entity.Video;

import java.util.List;

public interface VideoService {

    Video addVideo(Video v);

    List<Video> getVideoList(int page);

    void removeVideo(int id);
}
