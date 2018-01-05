package ru.nsu.fit.pm.scriptaur.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import okhttp3.Response;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nsu.fit.pm.scriptaur.dao.NoEntityException;
import ru.nsu.fit.pm.scriptaur.entity.Video;
import ru.nsu.fit.pm.scriptaur.entity.VideoUrl;
import ru.nsu.fit.pm.scriptaur.service.TokenService;
import ru.nsu.fit.pm.scriptaur.service.UserService;
import ru.nsu.fit.pm.scriptaur.service.VideoService;
import ru.nsu.fit.pm.scriptaur.util.YoutubeApi;

import java.util.Date;

@RestController
public class AddingVideoController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.PUT, params = {"token"})
    @ResponseBody
    public ResponseEntity addVideo(@RequestParam(value = "token") String token, @RequestBody(required = true) String videoUrl) {

        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        float trustFactor;
        try {
            trustFactor = userService.getUserTrustFactor(tokenService.getUserIdByToken(token));
        } catch (NoEntityException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (trustFactor < 0.4) return new ResponseEntity(HttpStatus.FORBIDDEN);

        Gson gson = new Gson();
        VideoUrl url = gson.fromJson(videoUrl, VideoUrl.class);

        Video video = null;
        try {
            video = videoCreator(url, tokenService.getUserIdByToken(token));
        } catch (NoEntityException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Video addedVideo = videoService.addVideo(video);

        if (addedVideo == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(addedVideo, HttpStatus.OK);
    }

    private static String API_KEY = "AIzaSyB1EKAPqyzYEDcLmTK5ZaqmRLwzgHB8kmc";

    private Video videoCreator(VideoUrl url, int userId) {
        Video video = new Video();
        video.setVideoUrl(url.getVideoUrl());
        video.setEvaluationsCount(0);
        video.setRating(0);
        video.setAddedOn(new Date());
        video.setAddedBy(userId);

        YoutubeApi client = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(YoutubeApi.class);

        String URL = video.getVideoUrl().replace("https://youtu.be/", "");
        retrofit2.Call<okhttp3.ResponseBody> call = client.getMetaInfo(API_KEY, "snippet, contentDetails", URL);
        String json = "";
        retrofit2.Response<okhttp3.ResponseBody> resp = null;
        try {
            resp = call.execute();
            okhttp3.ResponseBody bodyResponse = resp.body();
            if (bodyResponse != null) {
                json = bodyResponse.string();
            } else {
                return null;
            }

            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(json).getAsJsonObject();

            JsonArray itemsArray = object.getAsJsonArray("items");
            JsonObject details = itemsArray.get(0).getAsJsonObject().get("contentDetails").getAsJsonObject();
            String dur = details.get("duration").getAsString();
            boolean isCaptionAllowed = details.get("caption").getAsBoolean();

            JsonObject snippet = itemsArray.get(0).getAsJsonObject().get("snippet").getAsJsonObject();
            String imgUrl = snippet.get("thumbnails").getAsJsonObject().get("default").getAsJsonObject().get("url").getAsString();
            String title = snippet.get("title").getAsString();

            PeriodFormatter formatter = ISOPeriodFormat.standard();
            int seconds = formatter.parsePeriod(dur).toStandardSeconds().getSeconds();
            video.setImageUrl(imgUrl);
            video.setName(title);
            video.setLength(seconds);
            if (!isCaptionAllowed) return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return video;
    }
}
