package ru.nsu.fit.scriptaur.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    int userId;
    String username;
    float trustFactor;
    String trustFactorUpdated;

    public User(int id, String userName, float trustFactor, String trustFactorUpdated) {
        this.userId = id;
        this.username = userName;
        this.trustFactor = trustFactor;
        this.trustFactorUpdated = trustFactorUpdated;
    }

    protected User(Parcel in) {
        userId = in.readInt();
        username = in.readString();
        trustFactor = in.readFloat();
        trustFactorUpdated = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(username);
        dest.writeFloat(trustFactor);
        dest.writeString(trustFactorUpdated);
    }

    public int getId() {
        return userId;
    }

    public String getUserName() {
        return username;
    }

    public float getTrustFactor() {
        return trustFactor;
    }

    public String getTrustFactorUpdated() {
        return trustFactorUpdated;
    }
}
