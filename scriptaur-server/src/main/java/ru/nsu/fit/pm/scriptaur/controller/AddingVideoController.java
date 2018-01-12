package ru.nsu.fit.pm.scriptaur.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nsu.fit.pm.scriptaur.dao.NoEntityException;
import ru.nsu.fit.pm.scriptaur.entity.Video;
import ru.nsu.fit.pm.scriptaur.entity.VideoUrl;
import ru.nsu.fit.pm.scriptaur.service.TokenService;
import ru.nsu.fit.pm.scriptaur.service.UserService;
import ru.nsu.fit.pm.scriptaur.service.VideoService;
import ru.nsu.fit.pm.scriptaur.util.ProperVideoCreator;
import ru.nsu.fit.pm.scriptaur.util.TrustUpdater;
import ru.nsu.fit.pm.scriptaur.util.YoutubeApi;

import java.util.Date;
import java.util.List;

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


        int userId;
        try {
            userId = tokenService.getUserIdByToken(token);
        } catch (NoEntityException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }


        Date trustFactorUpdated = userService.getDateOfTrustFactorUpdated(userId);
        float trustFactorRes = 1;
        if (trustFactorUpdated == null) {
            //updateTrustFactor(userId);
            trustFactorRes = TrustUpdater.calculateTrustFactor(userId,
                    videoService.getVideoListLastMonthByUserId(userId));
            userService.updateTrustFactor(userId, trustFactorRes);
            userService.updateTrustFactorDay(userId);
        } else {

            Date curDate = new Date();
            //long dif = (curDate.getTime() - trustFactorUpdated.getTime()) / (1000 * 60 * 60 * 24);

            long diffMinutes = (curDate.getTime() - trustFactorUpdated.getTime()) / TrustUpdater.FACTOR;

            if (diffMinutes > TrustUpdater.TIMEOUT_TRUST_FACTOR_UPDATE) {
                //updateTrustFactor(userId);
                trustFactorRes = TrustUpdater.calculateTrustFactor(userId,
                        videoService.getVideoListLastMonthByUserId(userId));
                userService.updateTrustFactor(userId, trustFactorRes);
                userService.updateTrustFactorDay(userId);
            }
        }


        float trustFactor;
        try {
            trustFactor = userService.getUserTrustFactor(tokenService.getUserIdByToken(token));
            if (trustFactor == -1) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


        if (trustFactor < 0.4) return new ResponseEntity(HttpStatus.FORBIDDEN);

        Gson gson = new Gson();
        VideoUrl url = gson.fromJson(videoUrl, VideoUrl.class);

        Video video = null;
        try {
            video = videoCreator(url, tokenService.getUserIdByToken(token));
        } catch (NoEntityException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Video addedVideo;
        try {
            addedVideo = videoService.addVideo(video);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (addedVideo == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);


        return new ResponseEntity<>(addedVideo, HttpStatus.OK);
    }

    private static String API_KEY = "AIzaSyB1EKAPqyzYEDcLmTK5ZaqmRLwzgHB8kmc";


    private Video videoCreator(VideoUrl url, int userId) {

        Video video = new Video();
/*
        String properUrl = url.getVideoUrl()
                .replace("https://youtu.be/", "")
                .replace("youtu.be/", "")
                .replace("https://www.youtube.com/watch?v=", "")
                .replace("www.youtube.com/watch?v=", "")
                .replace("youtube.com/watch?v=", "");*/

        String properUrl = ProperVideoCreator.extractVideoIdFromUrl(url.getVideoUrl());

        video.setVideoUrl(properUrl);
        video.setEvaluationsCount(0);
        video.setRating(0);
        video.setAddedOn(new Date());
        video.setAddedBy(userId);

        YoutubeApi client = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(YoutubeApi.class);

        String URL = video.getVideoUrl();
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


    private void updateTrustFactor(int userId) {

        List<Video> videos = videoService.getVideoListLastMonthByUserId(userId);

        if (videos.size() == 0) {
            userService.updateTrustFactor(userId, 1);
            return;
        }

        float trustFactor = 0;

        int sum = 0;

        for (Video video : videos) {
            trustFactor += (video.getRating() - 1) / 4. * video.getEvaluationsCount();
            sum += video.getEvaluationsCount();
        }

        trustFactor /= sum;

        userService.updateTrustFactor(userId, trustFactor);

        userService.updateTrustFactorDay(userId);
    }


}
