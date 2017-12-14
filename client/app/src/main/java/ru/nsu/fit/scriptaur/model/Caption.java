package ru.nsu.fit.scriptaur.model;

public class Caption {
    private final int start;
    private final int duration;
    private final String text;


    public Caption(String start, String duration, String text) {
        this.start = timestampToMillis(start);
        this.duration = timestampToMillis(duration);
        this.text = text;

    }

    public int getStart() {
        return start;
    }

    public int getDuration() {
        return duration;
    }

    public String getText() {
        return text;
    }

    private int timestampToMillis(String timestamp) {
        double time = Double.parseDouble(timestamp);
        return (int) Math.round(time * 1000);
    }

    @Override
    public String toString() {
        return start + " " + duration + " " + text;
    }
}
