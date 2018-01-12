package ru.nsu.fit.pm.scriptaur.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "videos", uniqueConstraints = @UniqueConstraint(columnNames = "video_url"))
public class Video {

    @Id
    @Column(name = "video_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int videoId;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "added_by")
    private int addedBy;

    @Column(name = "added_on", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedOn;

    @Column(name = "rating")
    private float rating;

    @Column(name = "evaluations_count")
    private int evaluationsCount;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name = "name")
    private String name;


    @Column(name = "length")
    private int length;

    @Transient
    private Integer userMark;

    public Integer getUserMark() {
        return userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

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

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
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
