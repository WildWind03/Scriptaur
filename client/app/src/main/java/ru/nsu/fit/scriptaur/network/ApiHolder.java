package ru.nsu.fit.scriptaur.network;

public class ApiHolder {
    private static final String BASE_URL = "";
    private static Api ourInstance = RetrofitServiceFactory.createRetrofitService(Api.class, BASE_URL);

    public static Api getInstance() {
        return ourInstance;
    }

    private ApiHolder() {
    }
}
