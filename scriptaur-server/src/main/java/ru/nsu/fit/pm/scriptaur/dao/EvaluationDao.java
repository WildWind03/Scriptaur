package ru.nsu.fit.pm.scriptaur.dao;

import ru.nsu.fit.pm.scriptaur.entity.Evaluation;

import java.util.List;

public interface EvaluationDao {

    void addMark(int userId, int videoId, int mark);

    Evaluation getEvaluationByVideoId(int useId, int videoId);

    List<Evaluation> getEvaluationList(int page);


}
