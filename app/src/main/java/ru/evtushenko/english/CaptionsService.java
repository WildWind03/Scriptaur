package ru.evtushenko.english;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface CaptionsService {
    @GET("timedtext?lang=en")
    Observable<Response<ResponseBody>> text(@Query("v") String videoId);
}
