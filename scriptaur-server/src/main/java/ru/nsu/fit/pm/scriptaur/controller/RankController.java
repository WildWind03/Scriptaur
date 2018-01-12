package ru.nsu.fit.pm.scriptaur.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.entity.MarkData;
import ru.nsu.fit.pm.scriptaur.entity.Video;
import ru.nsu.fit.pm.scriptaur.service.EvaluationService;
import ru.nsu.fit.pm.scriptaur.service.TokenService;
import ru.nsu.fit.pm.scriptaur.service.UserService;
import ru.nsu.fit.pm.scriptaur.service.VideoService;
import ru.nsu.fit.pm.scriptaur.util.TrustUpdater;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/marks")
@ComponentScan("ru.nsu.fit.pm.scriptaur.service")
public class RankController {
    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    public void update(MarkData markData, int userId) {

        int diff = evaluationService.addMark(userId, markData.getVideoId(), markData.getMark());

        if (diff == 200) {
            videoService.updateEvaluationsCount(markData.getVideoId());
        }
        float rating = videoService.getVideoById(markData.getVideoId()).getRating();
        long evaluations_count = videoService.getEvaluationCount(markData.getVideoId());

        float new_mark = markData.getMark();
        float new_rating;
        if (diff == 200) {
            new_rating = (rating * (evaluations_count - 1) + new_mark) / (evaluations_count);
        } else {
            new_rating = (rating * evaluations_count + diff) / (evaluations_count);
        }
        videoService.updateVideoRating(markData.getVideoId(), new_rating);

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

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity addMark(@RequestParam(value = "token") String token, @RequestBody(required = true) String data) {

        if (!tokenService.checkTokenValidity(token)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        Gson gson = new Gson();
        MarkData markData = gson.fromJson(data, MarkData.class);


        try {
            int userId = tokenService.getUserIdByToken(token);
            update(markData, userId);

            int addedBy = videoService.getAuthorId(markData.getVideoId());

            Date trustFactorUpdated = userService.getDateOfTrustFactorUpdated(addedBy);
            if (trustFactorUpdated == null) {
                //updateTrustFactor(addedBy);
                float trustFactorRes = TrustUpdater.calculateTrustFactor(addedBy,
                        videoService.getVideoListLastMonthByUserId(addedBy));
                userService.updateTrustFactor(addedBy, trustFactorRes);
                userService.updateTrustFactorDay(addedBy);
            } else {

                Date curDate = new Date();
                //long dif = (curDate.getTime() - trustFactorUpdated.getTime()) / (1000 * 60 * 60 * 24);

                long diffMinutes = (curDate.getTime() - trustFactorUpdated.getTime()) / TrustUpdater.FACTOR;

                if (diffMinutes > TrustUpdater.TIMEOUT_TRUST_FACTOR_UPDATE) {
                    //updateTrustFactor(userId);
                    float trustFactorRes = TrustUpdater.calculateTrustFactor(addedBy,
                            videoService.getVideoListLastMonthByUserId(addedBy));
                    userService.updateTrustFactor(addedBy, trustFactorRes);
                    userService.updateTrustFactorDay(addedBy);
                }
            }

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
