package ru.nsu.fit.pm.scriptaur.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.entity.Video;
import ru.nsu.fit.pm.scriptaur.service.VideoService;
import ru.nsu.fit.pm.scriptaur.entity.VideoUrl;

import java.util.ArrayList;
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

        Video video = new Video();
        //toDo: find video by id

        if (video == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, params = {"page", "token"})
    @ResponseBody
    public ResponseEntity getVideos(@RequestParam(value = "page") int page, @RequestParam(value = "token") String token) {

        List<Video> videos = new ArrayList<>();
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


    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity addVideo(@RequestParam(value = "token") String token, @RequestBody(required = true) String videoUrl) {


        Gson gson = new Gson();
        VideoUrl url = gson.fromJson(videoUrl, VideoUrl.class);

        Video video = new Video();
        //toDo: adding new Video url (have only user's token??)

        if (video == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getUserVideos(@RequestParam(value = "token") String token) {

        List<Video> videos = new ArrayList<>();
        //toDo: find user's video by token

        if (videos == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(videos, HttpStatus.OK);

    }



}
