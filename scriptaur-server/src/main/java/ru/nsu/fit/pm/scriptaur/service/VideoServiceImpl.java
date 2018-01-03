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
}
