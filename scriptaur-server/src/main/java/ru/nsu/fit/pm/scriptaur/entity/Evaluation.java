package ru.nsu.fit.pm.scriptaur.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.javafx.beans.IDProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Column(name = "mark")
    private Integer mark;

    @EmbeddedId
    private EvaluationId evaluationId;

    public Evaluation(int mark, int userId, int videoId) {
        this.mark = mark;
        this.evaluationId = new EvaluationId(videoId, userId);
    }

    public Evaluation() {

    }

    public int getMark() {
        return mark;
    }

    public int getUserId() {
        return evaluationId.getUserId();
    }

    public int getVideoId() {
        return evaluationId.getVideoId();
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    @Embeddable
    public static class EvaluationId implements Serializable {
        public EvaluationId() {
        }

        @Column(name = "video_id")
        private int videoId;
        @Column(name = "user_id")
        private int userId;

        public EvaluationId(int videoId, int userId) {
            this.videoId = videoId;
            this.userId = userId;
        }

        int getVideoId() {
            return videoId;
        }

        int getUserId() {
            return userId;
        }
    }
}
