package ru.nsu.fit.pm.scriptaur.entity;


import javax.persistence.*;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @Column(name = "video_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int videoId;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "added_by")
    private int addedBy;

    @Column(name = "added_on")
    private String addedOn;

    @Column(name = "rating")
    private float rating;

    @Column(name = "evaluations_count")
    private int evaluationsCount;

    public int getVideoId() {
        return videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(int addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getEvaluationsCount() {
        return evaluationsCount;
    }

    public void setEvaluationsCount(int evaluationsCount) {
        this.evaluationsCount = evaluationsCount;
    }
}
