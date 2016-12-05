package net.args.mydailylook.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016-08-21.
 */
public class ServiceGenerator {
    public static final String API_BASE_URL = "http://mydailylook.net:3000/";
    public static final String DAUM_BASE_URL = "https://apis.daum.net/";

    private static MyDailyLookService myDailyLookService;
    private static DaumApiService daumApiService;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder builder2 =
            new Retrofit.Builder()
                    .baseUrl(DAUM_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());


    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    public static MyDailyLookService getService() {
        if (myDailyLookService == null) {
            Retrofit retrofit = builder.client(httpClient.build()).build();
            myDailyLookService = retrofit.create(MyDailyLookService.class);
        }

        return myDailyLookService;
    }

    public static DaumApiService getDaumApiService() {
        if (daumApiService == null) {
            Retrofit retrofit = builder2.client(httpClient.build()).build();
            daumApiService = retrofit.create(DaumApiService.class);
        }

        return daumApiService;
    }

}
