package com.paint.stockstore.service;

import com.paint.stockstore.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static RetrofitService instance;
    private static final String BASE_URL = "https://stocks-store-202.herokuapp.com/api/";
//    private static final String BASE_URL = "https://warm-citadel-97897.herokuapp.com/api/";
    private Retrofit retrofit;

//    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
//        @Override public Response intercept(Chain chain) throws IOException {
//            Response originalResponse = chain.proceed(chain.request());
//            if (Utils.isNetworkAvailable(context)) {
//                int maxAge = 60; // read from cache for 1 minute
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, max-age=" + maxAge)
//                        .build();
//            } else {
//                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .build();
//            }
//        }


    private RetrofitService() {

        //TODO delete logs
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //okHttpBuilder.addInterceptor(new TokenInterceptor());

        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(interceptor);
        }

        OkHttpClient client = okHttpBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static RetrofitService getInstance() {
        if (instance == null) {
            instance = new RetrofitService();
        }
        return instance;
    }

    public StockApi getApi() {
        return retrofit.create(StockApi.class);
    }
}