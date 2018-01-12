package ru.nsu.fit.pm.scriptaur.util;


import ru.nsu.fit.pm.scriptaur.entity.Video;

import java.util.List;

public class TrustUpdater {

    public static float calculateTrustFactor(int userId, List<Video> videos) {

        if (videos.size() == 0) {
            return 1;
        }

        float trustFactor = 0;

        int sum = 0;

        for (Video video : videos) {
            trustFactor += (video.getRating() - 1) / 4. * video.getEvaluationsCount();
            sum += video.getEvaluationsCount();
        }

        if (sum == 0) return  1;

        trustFactor /= sum;

        //userService.updateTrustFactor(userId, trustFactor);

        //userService.updateTrustFactorDay(userId);

        return trustFactor;
    }


    public static final long TIMEOUT_TRUST_FACTOR_UPDATE = 1;

    public static final long FACTOR = (60 * 1000) % 60;

}
