package ru.nsu.fit.pm.scriptaur.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.pm.scriptaur.dao.EvaluationDao;
import ru.nsu.fit.pm.scriptaur.entity.Evaluation;

import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationDao evaluationDao;

    @Transactional
    @Override
    public void addMark(int videoId, int mark) {
        this.evaluationDao.addMark(videoId, mark);
    }

    @Transactional
    @Override
    public Evaluation getEvaluationByVideoId(int videoId) {
        return this.evaluationDao.getEvaluationByVideoId(videoId);
    }

    @Transactional
    @Override
    public List<Evaluation> getEvaluationList(int page) {
        return this.evaluationDao.getEvaluationList(page);
    }
}
