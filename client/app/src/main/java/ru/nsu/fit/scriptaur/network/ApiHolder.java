package ru.nsu.fit.scriptaur.network;

public class ApiHolder {
    private static final String BACKEND_BASE_URL = "http://37.139.21.17:8080/";
    private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private static Api backendApiInstance = RetrofitServiceFactory.createRetrofitService(Api.class, BACKEND_BASE_URL);
    private static YoutubeApi youtubeApiInstance = RetrofitServiceFactory.createRetrofitService(YoutubeApi.class, YOUTUBE_BASE_URL);

    private ApiHolder() {
    }

    public static Api getBackendApi() {
        return backendApiInstance;
    }

    public static YoutubeApi getYoutubeApi() {
        return youtubeApiInstance;
    }
}
