package ru.nsu.fit.scriptaur.network;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;
import ru.nsu.fit.scriptaur.network.entities.MarkData;
import ru.nsu.fit.scriptaur.network.entities.SignUpData;
import ru.nsu.fit.scriptaur.network.entities.User;
import ru.nsu.fit.scriptaur.network.entities.Video;
import ru.nsu.fit.scriptaur.network.entities.VideoUrl;

public interface YoutubeApi {
    @GET("videos")
    Observable<Response<ResponseBody>> getMetaInfo(@Query("key") String apiKey, @Query("part") String part, @Query("id") String videoId);

    @GET
    Observable<Response<ResponseBody>> getIcon(@Url String url);
}
