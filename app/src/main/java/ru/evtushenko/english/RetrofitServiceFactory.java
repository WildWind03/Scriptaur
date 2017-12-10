package ru.evtushenko.english;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitServiceFactory {
    static <T> T createRetrofitService(final Class<T> clazz, final String baseUrl) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(clazz);
    }
}
