package com.dushane.weather

import android.media.Image
import com.dushane.weather.data.weather.WeatherResults
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherServices {

    @GET("onecall?units=imperial&appid=84c2b33596356c9fd60b69fc01a8afdf")
    fun getResults(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Single<WeatherResults>
}