package ru.nsu.fit.scriptaur.common.videos;

import android.os.Parcel;
import io.reactivex.Observable;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.entities.PagesCount;
import ru.nsu.fit.scriptaur.network.entities.Video;

import java.util.List;

public class AllVideosSource extends VideosSource {

    public static final Creator<AllVideosSource> CREATOR = new Creator<AllVideosSource>() {
        @Override
        public AllVideosSource createFromParcel(Parcel in) {
            return new AllVideosSource(in);
        }

        @Override
        public AllVideosSource[] newArray(int size) {
            return new AllVideosSource[size];
        }
    };
    private String userToken = "";

    public AllVideosSource(String userToken) {
        this.userToken = userToken;
    }

    public AllVideosSource(Parcel in) {
        userToken = in.readString();
    }

    @Override
    public Observable<List<Video>> getPage(final int pageId) {
        return ApiHolder.getBackendApi().getVideos(pageId, userToken);
    }

    @Override
    public Observable<PagesCount> pagesCount() {
        return ApiHolder.getBackendApi().getVideosPagesCount(userToken);
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
