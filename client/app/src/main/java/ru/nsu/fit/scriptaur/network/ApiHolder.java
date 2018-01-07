package ru.nsu.fit.scriptaur.network;

public class ApiHolder {
    private static final String BACKEND_BASE_URL = "http://37.139.21.17:8080/";
    private static Api backendApiInstance = RetrofitServiceFactory.createRetrofitService(Api.class, BACKEND_BASE_URL);


    private ApiHolder() {
    }

    public static Api getBackendApi() {
        return backendApiInstance;
    }

}
