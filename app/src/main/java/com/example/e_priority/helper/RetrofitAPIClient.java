package com.example.e_priority.helper;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.e_priority.constants.Constants;
import com.example.e_priority.session.SessionManager;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPIClient {

    public static Retrofit retrofit = null;

//    public static Retrofit getClient() {
//        CookieManager cookieManager = new CookieManager();
//        cookieManager = (CookieManager) Helper.getItemParam(Constants.COOKIE_MANAGER);
//        CookieStore cookieStore = cookieManager.getCookieStore();
//        List<HttpCookie> cookieList = cookieStore.getCookies();
//        CookieJar cookieJar = new JavaNetCookieJar(cookieManager);
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .cookieJar(cookieJar)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .addInterceptor(interceptor).build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(Helper.getItemParam(Constants.BASE_URL).toString())
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        return retrofit;
//    }

    public static Retrofit getClientWithoutCookies() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Helper.getItemParam(Constants.BASE_URL).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getClientWithToken() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    requestBuilder.header("Content-Type", "application/json");
                    requestBuilder.header("Authorization", Constants.BEARER.concat((String) Helper.getItemParam(Constants.TOKEN)));
                    return chain.proceed(requestBuilder.build());
                }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Helper.getItemParam(Constants.BASE_URL).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }




}
