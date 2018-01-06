package ru.nsu.fit.scriptaur.network.entities;

public class MarkData {
    private int videoId;
    private int mark;
    public MarkData(int videoId, int mark) {
        this.videoId = videoId;
        this.mark = mark;
    }

    public int getVideoId() {
        return videoId;
    }

    public int getMark() {
        return mark;
    }
}
