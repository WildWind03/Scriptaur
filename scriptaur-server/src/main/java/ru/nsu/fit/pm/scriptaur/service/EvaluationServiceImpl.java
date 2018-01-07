package ru.nsu.fit.pm.scriptaur.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.pm.scriptaur.dao.EvaluationDao;
import ru.nsu.fit.pm.scriptaur.entity.Evaluation;

import java.util.List;

@Service
@Transactional
@ComponentScan("ru.nsu.fit.scriptaur.dao")
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationDao evaluationDao;

    @Transactional
    @Override
    public int addMark(int userId, int videoId, int mark) {
        return this.evaluationDao.addMark(userId, videoId, mark);
    }

    @Transactional
    @Override
    public Evaluation getEvaluationByVideoId(int userId, int videoId) {
        return this.evaluationDao.getEvaluationByVideoId(userId, videoId);
    }

    @Transactional
    @Override
    public List<Evaluation> getEvaluationList(int page) {
        return this.evaluationDao.getEvaluationList(page);
    }


    @Transactional
    @Override
    public Integer getUserMarkByVideoId(int userId, int videoId) {
        return evaluationDao.getUserMarkByVideoId(userId, videoId);
    }
}
