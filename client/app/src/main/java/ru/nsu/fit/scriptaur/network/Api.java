package ru.nsu.fit.scriptaur.network;

import io.reactivex.Observable;
import retrofit2.http.*;
import ru.nsu.fit.scriptaur.network.entities.*;

import java.util.List;

public interface Api {

    @GET("videosCount")
    Observable<List<Video>> getPagesCount(@Query("token") String token);

    @GET("videos")
    Observable<List<Video>> getVideos(@Query("page") int page, @Query("token") String token);

    @GET("videos")
    Observable<Video> getVideo(@Query("video_id") int id, @Query("token") String token);

    @GET("videos")
    Observable<List<Video>> findVideo(@Query("search") String request, @Query("token") String token);

    @GET("users")
    Observable<User> getUser(@Query("user_id") int id, @Query("token") String token);

    @POST("signup")
    Observable<User> signUp(@Body SignUpData data);

    @POST("signin")
    Observable<User> signIn(@Body SignUpData data);

    @GET("signout")
    Observable<User> signOut(@Query("token") String token);

    @PUT("users")
    Observable<User> changePassword(@Query("token") String token, @Body SignUpData data);

    @PUT("marks")
    Observable<Video> addMark(@Query("token") String token, @Body MarkData data);

    @PUT("videos")
    Observable<Video> addVideo(@Query("token") String token, @Body VideoUrl videoUrl);

    @GET("videos")
    Observable<List<Video>> getUserVideos(@Query("token") String token);
}
