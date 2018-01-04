package ru.nsu.fit.scriptaur.common.videos;

import android.os.Parcelable;
import io.reactivex.Observable;
import ru.nsu.fit.scriptaur.network.entities.PagesCount;
import ru.nsu.fit.scriptaur.network.entities.Video;

import java.util.List;

abstract public class VideosSource implements Parcelable {
    abstract public Observable<List<Video>> getPage(int pageId);

    abstract public Observable<PagesCount> pagesCount();
}
