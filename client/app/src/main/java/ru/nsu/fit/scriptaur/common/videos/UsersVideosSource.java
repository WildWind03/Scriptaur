package ru.nsu.fit.scriptaur.common.videos;

import android.os.Parcel;

import java.util.List;

import io.reactivex.Observable;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.entities.PagesCount;
import ru.nsu.fit.scriptaur.network.entities.Video;

public class UsersVideosSource extends VideosSource {

    public static final Creator<UsersVideosSource> CREATOR = new Creator<UsersVideosSource>() {
        @Override
        public UsersVideosSource createFromParcel(Parcel in) {
            return new UsersVideosSource(in);
        }

        @Override
        public UsersVideosSource[] newArray(int size) {
            return new UsersVideosSource[size];
        }
    };
    private String userToken = "";

    public UsersVideosSource(String userToken) {
        this.userToken = userToken;
    }

    public UsersVideosSource(Parcel in) {
        userToken = in.readString();
    }

    @Override
    public Observable<List<Video>> getPage(final int pageId) {
        return ApiHolder.getBackendApi().getUserVideos(pageId, userToken);
    }

    @Override
    public Observable<PagesCount> pagesCount() {
        return ApiHolder.getBackendApi().getUserVideosPagesCount(userToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userToken);
    }
}
