package com.essensys.JB089.Utility;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by admin on 22-03-2018.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String endPoint) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                //.baseUrl(BuildConfig.SERVER_URL + endPoint)
                .baseUrl(endPoint+"/")
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

//    public static Retrofit getRetrofitClient() {
//        retrofit = new Retrofit.Builder()
//                .baseUrl()
//                .build();
//        return retrofit;
//    }

    public static RequestBody getRequestBodyFromString(String str) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), str);
    }

    public static RequestBody getRequestBodyFromStringFile(String str) {
        File file = new File(str);
        RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), file);
        return fileBody;
    }

    public static RequestBody getRequestBodyFromFile(File file) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), file);
        return fileBody;
    }

}
