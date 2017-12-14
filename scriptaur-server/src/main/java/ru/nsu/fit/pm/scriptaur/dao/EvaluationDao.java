package ru.nsu.fit.pm.scriptaur.dao;

import ru.nsu.fit.pm.scriptaur.entity.Evaluation;

import java.util.List;

public interface EvaluationDao {

    void addMark(int videoId, int mark);

    Evaluation getEvaluationByVideoId(int videoId);

    List<Evaluation> getEvaluationList(int page);

}
