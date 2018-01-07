package ru.nsu.fit.scriptaur.network.entities;


import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
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
    private String imageUrl;
    private String name;
    private int length;
    private Integer userMark;

    public Video(int videoId, String videoUrl, String name, String imageUrl, int length,
                 int addedBy, String addedOn, float rating, int evaluationsCount, int userMark) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.name = name;
        this.imageUrl = imageUrl;
        this.length = length;
        this.addedBy = addedBy;
        this.addedOn = addedOn;
        this.rating = rating;
        this.evaluationsCount = evaluationsCount;
        this.userMark = userMark;
    }

    protected Video(Parcel in) {
        videoId = in.readInt();
        videoUrl = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        length = in.readInt();
        addedBy = in.readInt();
        addedOn = in.readString();
        rating = in.readFloat();
        evaluationsCount = in.readInt();
        userMark = in.readInt();
    }

    public int getVideoId() {
        return videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getLength() {
        return length;
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

    public Integer getUserMark() {
        return userMark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(videoId);
        dest.writeString(videoUrl);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeInt(length);
        dest.writeInt(addedBy);
        dest.writeString(addedOn);
        dest.writeFloat(rating);
        dest.writeInt(evaluationsCount);
        dest.writeInt(userMark);
    }
}
