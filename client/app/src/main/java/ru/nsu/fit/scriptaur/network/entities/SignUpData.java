package ru.nsu.fit.scriptaur.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class SignUpData implements Parcelable {
    private String username;
    private String password;
    private String name;

    public SignUpData(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    protected SignUpData(Parcel in) {
        username = in.readString();
        password = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignUpData> CREATOR = new Creator<SignUpData>() {
        @Override
        public SignUpData createFromParcel(Parcel in) {
            return new SignUpData(in);
        }

        @Override
        public SignUpData[] newArray(int size) {
            return new SignUpData[size];
        }
    };
}
