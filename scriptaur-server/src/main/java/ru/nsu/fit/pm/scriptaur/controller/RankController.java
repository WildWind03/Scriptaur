package ru.nsu.fit.pm.scriptaur.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.entity.MarkData;
import ru.nsu.fit.pm.scriptaur.entity.User;
import ru.nsu.fit.pm.scriptaur.entity.Video;
import ru.nsu.fit.pm.scriptaur.service.EvaluationService;
import ru.nsu.fit.pm.scriptaur.service.UserService;
import ru.nsu.fit.pm.scriptaur.service.VideoService;

import java.util.List;

@RestController
@RequestMapping("/marks")
@ComponentScan("ru.nsu.fit.pm.scriptaur.service")
public class RankController {
    @Autowired
    EvaluationService evaluationService;

    @Autowired
    VideoService videoService;

    @Autowired
    UserService userService;

    @Autowired
    ApplicationContext context;

    public void update(MarkData markData, String token) {

        //toDo: process token

        User user = findUser(token);

        evaluationService.addMark(user.getUserId(), markData.getVideoId(), markData.getMark());
        videoService.updateEvaluationsCount(markData.getVideoId());


        float rating = videoService.getVideoById(markData.getVideoId()).getRating();
        long evaluations_count = videoService.getEvaluationCount(markData.getVideoId());
        float new_mark = markData.getMark();
        float new_rating = (rating * evaluations_count + new_mark) / (evaluations_count + 1);
        videoService.updateVideoRating(markData.getVideoId(), new_rating);

        updateTrustFactor(user.getUserId());

    }

    private User findUser(String token) {
        return userService.getUserById(1);
    }

    private void updateTrustFactor(int userId) {

        List<Video> videos = videoService.getVideoListByUserId(userId);

        float trustFactor = 0;

        int sum = 0;

        for (Video video : videos) {
            trustFactor += (video.getRating() - 1) / 4. * video.getEvaluationsCount();
            sum += video.getEvaluationsCount();
        }

        trustFactor /= sum;

        userService.updateTrustFactor(userId, trustFactor);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity addMark(@RequestParam(value = "token") String token, @RequestBody(required = true) String data) {

        Gson gson = new Gson();
        MarkData markData = gson.fromJson(data, MarkData.class);


        update(markData, token);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
