package ru.nsu.fit.pm.scriptaur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.nsu.fit.pm.scriptaur.dao.NoEntityException;
import ru.nsu.fit.pm.scriptaur.entity.Video;
import ru.nsu.fit.pm.scriptaur.service.TokenService;
import ru.nsu.fit.pm.scriptaur.service.VideoService;

import java.util.List;

@RestController
@RequestMapping("/videos")
@ComponentScan("ru.nsu.fit.pm.scriptaur.service")
public class VideoContoller {

    @Autowired
    private VideoService videoService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(method = RequestMethod.GET, params = {"video_id", "token"})
    @ResponseBody
    public ResponseEntity getVideo(@RequestParam(value = "video_id") int id, @RequestParam(value = "token") String token) {

        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        Video video = videoService.getVideoById(id);

        if (video == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, params = {"page", "token"})
    @ResponseBody
    public ResponseEntity getVideos(@RequestParam(value = "page") int page, @RequestParam(value = "token") String token) {

        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        List<Video> videos = videoService.getAllVideosByPage(page);

        if (videos == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(videos, HttpStatus.OK);

    }



    @RequestMapping(method = RequestMethod.GET, params = {"token", "page"})
    @ResponseBody
    public ResponseEntity getUserVideos(@RequestParam(value = "token") String token, @RequestParam(value = "page") int page) {

        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        int userId = 0;
        try {
            userId = tokenService.getUserIdByToken(token);
        } catch (NoEntityException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<Video> videos = videoService.getVideoListLastMonthByUserId(userId, page);

        if (videos == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(videos, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.GET, params = {"token", "page", "query"})
    @ResponseBody
    public ResponseEntity findVideo(@RequestParam(value = "token") String token, @RequestParam(value = "page") int page, @RequestParam(value = "query") String query) {

        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        List<Video> videos = videoService.findVideoList(page, query);

        if (videos == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(videos, HttpStatus.OK);

    }


}
