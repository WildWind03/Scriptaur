package ru.nsu.fit.scriptaur.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    int id;
    String userName;
    float trustFactor;
    String trustFactorUpdated;

    public User(int id, String userName, float trustFactor, String trustFactorUpdated) {
        this.id = id;
        this.userName = userName;
        this.trustFactor = trustFactor;
        this.trustFactorUpdated = trustFactorUpdated;
    }

    protected User(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        trustFactor = in.readFloat();
        trustFactorUpdated = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userName);
        dest.writeFloat(trustFactor);
        dest.writeString(trustFactorUpdated);
    }
}
