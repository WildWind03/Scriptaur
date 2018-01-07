package ru.nsu.fit.pm.scriptaur.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.entity.PagesCount;
import ru.nsu.fit.pm.scriptaur.service.TokenService;
import ru.nsu.fit.pm.scriptaur.service.VideoService;

@RestController
@ComponentScan("ru.nsu.fit.pm.scriptaur.service")
public class CountController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private TokenService tokenService;


    @RequestMapping(method = RequestMethod.GET, value = "videosCount", params = {"token"})
    @ResponseBody
    public ResponseEntity getVideosPagesCount(@RequestParam(value = "token") String token) {

        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        int count = videoService.getCountOfPagesVideos();

        PagesCount pagesCount = new PagesCount();
        pagesCount.setPagesCount(count);

        return new ResponseEntity<>(pagesCount, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.GET, value = "userVideosCount", params = {"token"})
    @ResponseBody
    public ResponseEntity getUserVideosPagesCount(@RequestParam(value = "token") String token) {

        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        int user_id = tokenService.getUserIdByToken(token);

        int count = videoService.getCountOfPagesVideosByUserId(user_id);

        PagesCount pagesCount = new PagesCount();
        pagesCount.setPagesCount(count);

        return new ResponseEntity<>(pagesCount, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, value = "queryVideosCount", params = {"token", "query"})
    @ResponseBody
    public ResponseEntity getUserVideosPagesCount(@RequestParam(value = "token") String token, @RequestParam(value = "query") String query) {


        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        int count = videoService.getCountOfPagesVideosByQuery(query);

        PagesCount pagesCount = new PagesCount();
        pagesCount.setPagesCount(count);

        return new ResponseEntity<>(pagesCount, HttpStatus.OK);


    }


}
