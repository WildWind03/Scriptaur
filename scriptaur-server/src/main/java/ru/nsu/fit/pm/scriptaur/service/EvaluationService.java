package ru.nsu.fit.pm.scriptaur.service;

import ru.nsu.fit.pm.scriptaur.entity.Evaluation;

import java.util.List;

public interface EvaluationService {

    void addMark(int videoId, int mark);

    Evaluation getEvaluationByVideoId(int videoId);

    List<Evaluation> getEvaluationList(int page);
}
