package ru.nsu.fit.scriptaur.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.*;
import ru.nsu.fit.scriptaur.network.entities.*;

import java.util.List;

public interface Api {
    //gets number of pages of all videos from database
    @GET("videosCount")
    Observable<PagesCount> getVideosPagesCount(@Query("token") String token);

    //gets all videos from database
    @GET("videos")
    Observable<List<Video>> getVideos(@Query("page") int page, @Query("token") String token);

    //gets number of pages of videos added by specified user
    @GET("userVideosCount")
    Observable<PagesCount> getUserVideosPagesCount(@Query("token") String token);

    //gets all videos added by specified user
    @GET("videos")
    Observable<List<Video>> getUserVideos(@Query("token") String token);

    //gets number of pages of videos that matches query
    @GET("queryVideosCount")
    Observable<PagesCount> getQueryVideosPagesCount(@Query("query") String query, @Query("token") String token);

    //gets videos that matches query
    @GET("videos")
    Observable<List<Video>> findVideo(@Query("query") String query, @Query("page") int page, @Query("token") String token);

    @GET("users")
    Observable<User> getUser(@Query("token") String token);

    @POST("signup")
    Observable<UserToken> signUp(@Body SignUpData data);

    @POST("signin")
    Observable<UserToken> signIn(@Body SignUpData data);

    @GET("signout")
    Observable<ResponseBody> signOut(@Query("token") String token);

    @PUT("users")
    Observable<ResponseBody> changePassword(@Query("token") String token, @Body SignUpData data);

    @PUT("marks")
    Observable<Video> addMark(@Query("token") String token, @Body MarkData data);

    @PUT("videos")
    Observable<Video> addVideo(@Query("token") String token, @Body VideoUrl videoUrl);


}
