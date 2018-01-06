package ru.nsu.fit.scriptaur.common.videos;

import android.os.Parcel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import ru.nsu.fit.scriptaur.network.entities.PagesCount;
import ru.nsu.fit.scriptaur.network.entities.Video;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DummyVideoSource extends VideosSource {
    public static final Creator<DummyVideoSource> CREATOR = new Creator<DummyVideoSource>() {
        @Override
        public DummyVideoSource createFromParcel(Parcel in) {
            return new DummyVideoSource(in);
        }

        @Override
        public DummyVideoSource[] newArray(int size) {
            return new DummyVideoSource[size];
        }
    };
    private static final Map<Integer, Video> dummyVideos = new TreeMap<>();

    static {
        dummyVideos.put(0, new Video(1, "VNqNnUJVcVs", "video 1", "image url 1", 100, 0, "0", 4.5f, 10, 1));
        dummyVideos.put(1, new Video(2, "CW5oGRx9CLM", "video 2", "https://i.ytimg.com/vi/CW5oGRx9CLM/mqdefault.jpg", 200, 0, "0", 5.0f, 15, 2));
        dummyVideos.put(2, new Video(3, "FBnAZnfNB6U", "video 3", "image url 3", 300, 0, "0", 1.488f, 1, 5));
    }

    public DummyVideoSource() {
    }

    public DummyVideoSource(Parcel in) {
    }

    @Override
    public Observable<List<Video>> getPage(final int pageId) {
        return new Observable<List<Video>>() {

            @Override
            protected void subscribeActual(Observer<? super List<Video>> observer) {
                observer.onNext(Collections.nCopies(15, dummyVideos.get(pageId)));
                observer.onComplete();
            }
        };
    }

    @Override
    public Observable<PagesCount> pagesCount() {
        return new Observable<PagesCount>() {
            @Override
            protected void subscribeActual(Observer<? super PagesCount> observer) {
                observer.onNext(new PagesCount(3));
                observer.onComplete();
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
