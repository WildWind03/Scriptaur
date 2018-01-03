package ru.nsu.fit.pm.scriptaur.entity;

public class MarkData {
    int videoId;
    int mark;

    @Override
    public String toString() {
        return "MarkData{" +
                "videoId=" + videoId +
                ", mark=" + mark +
                '}';
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getMark() {

        return mark;
    }
}
