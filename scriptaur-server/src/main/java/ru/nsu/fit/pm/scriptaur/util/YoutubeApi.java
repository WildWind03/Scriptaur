package ru.nsu.fit.pm.scriptaur.util;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface YoutubeApi {

    @GET("videos")
    Call<ResponseBody> getMetaInfo(@Query("key") String apiKey, @Query("part") String part, @Query("id") String videoId);

    @GET
    Call<ResponseBody> getIcon(@Url String url);
}
