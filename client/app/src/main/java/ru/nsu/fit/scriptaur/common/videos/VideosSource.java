package ru.nsu.fit.scriptaur.common.videos;

import android.os.Parcelable;

import java.util.List;

import io.reactivex.Observable;
import ru.nsu.fit.scriptaur.network.entities.Video;

abstract public class VideosSource implements Parcelable{
    abstract public Observable<List<Video>> getPage(int pageId);
    abstract public Observable<Integer> pagesCount();
}
