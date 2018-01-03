package ru.nsu.fit.scriptaur.network.entities;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable, Comparable<Video> {
    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {

        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
    private int videoId;
    private String videoUrl;
    private int addedBy;
    private String addedOn;
    private float rating;
    private int evaluationsCount;
    private boolean isRated;

    public Video() {
    }

    public Video(int videoId, String videoUrl, int addedBy, String addedOn, float rating, int evaluationsCount, boolean isRated) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.addedBy = addedBy;
        this.addedOn = addedOn;
        this.rating = rating;
        this.evaluationsCount = evaluationsCount;
        this.isRated = isRated;
    }

    public Video(Parcel source) {
        videoId = source.readInt();
        videoUrl = source.readString();
        addedBy = source.readInt();
        addedOn = source.readString();
        rating = source.readFloat();
        evaluationsCount = source.readInt();
        isRated = source.readInt() == 1;
    }

    @Override
    public int compareTo(@NonNull Video video) {
        return hashCode() - video.hashCode();
    }

    public int getVideoId() {
        return videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public int getAddedBy() {
        return addedBy;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public float getRating() {
        return rating;
    }

    public int getEvaluationsCount() {
        return evaluationsCount;
    }

    public boolean isRated() {
        return isRated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(videoId);
        dest.writeString(videoUrl);
        dest.writeInt(addedBy);
        dest.writeString(addedOn);
        dest.writeFloat(rating);
        dest.writeInt(evaluationsCount);
        // Why there is no write bool :I
        dest.writeInt(isRated ? 1 : 0);
    }
}
