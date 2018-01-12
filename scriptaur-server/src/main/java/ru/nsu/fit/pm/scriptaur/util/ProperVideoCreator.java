package ru.nsu.fit.pm.scriptaur.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProperVideoCreator {

    private static final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
    private static final String[] videoIdRegex = {"\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9\\-\\_]*)"};
    //private final String[] videoIdRegex1 = {"\\?vi?=([^&])",  "watch\\?.v=([^&])",   "(?:embed|vi?)/([^/?])",  "^([A-Za-z0-9\\-\\_]*)"};

    public static void main(String[] args) {

        System.out.println(extractVideoIdFromUrl("https://www.youtube.com/watch?v=p-N_y1bZtRw"));
        System.out.println(extractVideoIdFromUrl("https://youtube.com/watch?v=Yaw8G8VAAJ8"));
        System.out.println(extractVideoIdFromUrl("https://m.youtube.com/watch?v=Yaw8G8VAAJ8"));
        System.out.println(extractVideoIdFromUrl("https://youtu.be/Yaw8G8VAAJ8"));
        System.out.println(extractVideoIdFromUrl("youtu.be/Yaw8G8VAAJ8"));
        System.out.println(extractVideoIdFromUrl("Yaw8G8VAAJ8"));


      /*  Date curDate = new Date();
        Date trustFactorUpdated = new Date(new Date().getTime() + 10 * ONE_MINUTE_IN_MILLIS);


        long diffMinutes = (curDate.getTime() - trustFactorUpdated.getTime()) / (60 * 1000) % 60;

        System.out.println(curDate.getTime());

        System.out.println(diffMinutes);
*/
        //1515591548633
    }
    //static final long ONE_MINUTE_IN_MILLIS=60000;

    public static String extractVideoIdFromUrl(String url) {
        String youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url);

        for (String regex : videoIdRegex) {
            Pattern compiledPattern = Pattern.compile(regex);
            Matcher matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

    private static String youTubeLinkWithoutProtocolAndDomain(String url) {
        Pattern compiledPattern = Pattern.compile(youTubeUrlRegEx);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return url.replace(matcher.group(), "");
        }
        return url;
    }
}
