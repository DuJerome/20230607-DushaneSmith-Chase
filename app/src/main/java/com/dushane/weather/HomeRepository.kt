package com.dushane.weather

import com.dushane.weather.data.location.LocationResults
import com.dushane.weather.data.location.Results
import com.dushane.weather.data.weather.WeatherResults
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val weatherServices: WeatherServices,
    private val geocodeServices: GeocodeServices
){
    fun getWeatherResults(lat: String, lon: String): Single<WeatherResults>{
        return weatherServices.getResults(lat, lon)
    }

    fun getLocationResults(location: String): Single<LocationResults>{
        return geocodeServices.getLocation(location)
    }
}