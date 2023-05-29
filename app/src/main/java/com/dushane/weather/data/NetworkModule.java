package com.dushane.weather.data;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(){
        return new OkHttpClient()
                .newBuilder()
                .build();
    }

    @Singleton
    @Provides
    public Gson provideGSON(){
        return new Gson().newBuilder()
                .create();
    }

    @Singleton
    @Provides
    public String provideBaseUrl() {
        return "https://api.openweathermap.org/data/3.0/";
    }
//https://maps.googleapis.com/maps/api/geocode/
    @Singleton
    @Provides
    public Retrofit provideRetrofit(
            String baseUrl
    ){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }
}
