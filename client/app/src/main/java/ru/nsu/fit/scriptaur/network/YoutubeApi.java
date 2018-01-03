package ru.nsu.fit.scriptaur.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface YoutubeApi {
    @GET("videos")
    Observable<Response<ResponseBody>> getMetaInfo(@Query("key") String apiKey, @Query("part") String part, @Query("id") String videoId);

    @GET
    Observable<Response<ResponseBody>> getIcon(@Url String url);
}
