package ru.nsu.fit.pm.scriptaur.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.entity.Video;
import ru.nsu.fit.pm.scriptaur.service.VideoService;
import ru.nsu.fit.pm.scriptaur.entity.VideoUrl;
import ru.nsu.fit.pm.scriptaur.util.YoutubeApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/videos")
@ComponentScan("ru.nsu.fit.pm.scriptaur.service")
public class VideoContoller {

    @Autowired
    private VideoService videoService;

    @RequestMapping(method = RequestMethod.GET, params = {"video_id", "token"})
    @ResponseBody
    public ResponseEntity getVideo(@RequestParam(value = "video_id") int id, @RequestParam(value = "token") String token) {
        System.out.println("one");

        Video video = videoService.getVideoById(id);
        //toDo: process token

        if (video == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, params = {"page", "token"})
    @ResponseBody
    public ResponseEntity getVideos(@RequestParam(value = "page") int page, @RequestParam(value = "token") String token) {

        List<Video> videos = videoService.getAllVideosByPage(page);
        //toDo: find list of videos by page number

        if (videos == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(videos, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, params = {"request", "token"})
    @ResponseBody
    public ResponseEntity findVideo(@RequestParam(value = "search") String request, @RequestParam(value = "token") String token) {

        List<Video> videos = new ArrayList<>();
        //toDo: find list of videos by name (!!!)

        if (videos == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(videos, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.PUT, params = {"token", "videoUrl"})
    @ResponseBody
    public ResponseEntity addVideo(@RequestParam(value = "token") String token, @RequestBody(required = true) String videoUrl) {


        Gson gson = new Gson();
        VideoUrl url = gson.fromJson(videoUrl, VideoUrl.class);

        Video video = videoCreator(url, 1);

        Video addedVideo = videoService.addVideo(video);

        //toDo: adding new Video url (have only user's token??)

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
        Call<okhttp3.ResponseBody> call = client.getMetaInfo(API_KEY, "snippet, contentDetails", URL);
        String json = "";
        Response<okhttp3.ResponseBody> resp = null;
        try {
            resp = call.execute();
            okhttp3.ResponseBody bodyResponse = resp.body();
            json = bodyResponse.string();

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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        return video;
    }


    @RequestMapping(method = RequestMethod.GET, params = {"token", "page"})
    @ResponseBody
    public ResponseEntity getUserVideos(@RequestParam(value = "token") String token, @RequestParam(value = "page") int page) {

        //List<Video> videos = new ArrayList<>();
        //toDo: find user's video by token

        int userId = 1;
        List<Video> videos = videoService.getVideoListByUserId(userId, page);

        if (videos == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(videos, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.GET, params = {"token", "page", "query"})
    @ResponseBody
    public ResponseEntity findVideo(@RequestParam(value = "token") String token, @RequestParam(value = "page") int page, @RequestParam(value = "query") String query) {

        List<Video> videos = videoService.findVideoList(page, query);

        if (videos == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(videos, HttpStatus.OK);

    }




}
