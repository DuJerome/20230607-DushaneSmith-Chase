package com.dushane.weather.data

import com.dushane.weather.data.location.LocationResults
import com.dushane.weather.data.location.Results
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
// a interface for turning address information into longitude and latitude using an api
interface GeocodeServices {

    @GET("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyDgTcqSwMpIRTRTJV7D2sV9yZXEejIYmNM")
    fun getLocation(
        @Query("address") address: String
    ): Single<LocationResults>
}