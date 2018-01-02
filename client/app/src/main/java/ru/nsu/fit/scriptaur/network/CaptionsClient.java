package ru.nsu.fit.scriptaur.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface CaptionsClient {
    @GET("timedtext?lang=en")
    Observable<Response<ResponseBody>> text(@Query("v") String videoId);
}
