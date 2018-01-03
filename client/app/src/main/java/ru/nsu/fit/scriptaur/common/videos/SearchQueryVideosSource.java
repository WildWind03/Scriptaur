package ru.nsu.fit.scriptaur.common.videos;

import android.os.Parcel;

import java.util.List;

import io.reactivex.Observable;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.entities.PagesCount;
import ru.nsu.fit.scriptaur.network.entities.Video;

public class SearchQueryVideosSource extends VideosSource {

    public static final Creator<SearchQueryVideosSource> CREATOR = new Creator<SearchQueryVideosSource>() {
        @Override
        public SearchQueryVideosSource createFromParcel(Parcel in) {
            return new SearchQueryVideosSource(in);
        }

        @Override
        public SearchQueryVideosSource[] newArray(int size) {
            return new SearchQueryVideosSource[size];
        }
    };
    private String userToken = "";
    private String query = "";

    public SearchQueryVideosSource(String userToken, String query) {
        this.userToken = userToken;
        this.query = query;
    }

    public SearchQueryVideosSource(Parcel in) {
        userToken = in.readString();
        query = in.readString();
    }

    @Override
    public Observable<List<Video>> getPage(final int pageId) {
        return ApiHolder.getBackendApi().findVideo(query, pageId, userToken);
    }

    @Override
    public Observable<PagesCount> pagesCount() {
        return ApiHolder.getBackendApi().getQueryVideosPagesCount(query, userToken);
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
