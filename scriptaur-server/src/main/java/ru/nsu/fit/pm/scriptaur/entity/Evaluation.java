package ru.nsu.fit.pm.scriptaur.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "evaluations")
public class Evaluation {


    @Column(name = "video_id")
    private int videoId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "mark")
    private int mark;


    public Evaluation(int videoId, int userId, int mark) {
        this.videoId = videoId;
        this.userId = userId;
        this.mark = mark;
    }

    public int getVideoId() {
        return videoId;
    }

    public int getUserId() {
        return userId;
    }

    public int getMark() {
        return mark;
    }
}
