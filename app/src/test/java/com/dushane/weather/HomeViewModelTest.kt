package com.dushane.weather

import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

internal class HomeViewModelTest {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl("https://api.openweathermap.org/data/3.0/")
        .build();

    private val geocodeServices = retrofit.create(GeocodeServices::class.java)
    private val weatherServices = retrofit.create(WeatherServices::class.java)
    private val homeRepository: HomeRepository = HomeRepository(weatherServices, geocodeServices)
    private val homeViewModel: HomeViewModel = HomeViewModel(homeRepository)

    @Test
    fun getCurrentLocationResultsForZipcode() {
        val zipcode = "30134"

        val locationData = homeViewModel.getLocationResults(zipcode)

        assertThat(locationData.results).isNotEmpty()
        assertThat(locationData.results[0].geometry).isNotNull()
        assertThat(locationData.results[0].formattedAddress).isEqualTo("Douglasville, GA 30134, USA")
        assertThat(locationData.results[0].addressComponents[0].longName).isEqualTo(zipcode)

        assertThat(locationData).isEqualTo(homeViewModel.currentLocationResults.value)
    }

    @Test
    fun getCurrentWeatherResultsForZipcode() {
        val zipcode = "30134"

        val locationData = homeViewModel.getLocationResults(zipcode)

        assertThat(locationData.results).isNotEmpty()
        assertThat(locationData.results[0].geometry).isNotNull()
        assertThat(locationData.results[0].formattedAddress).isEqualTo("Douglasville, GA 30134, USA")
        assertThat(locationData.results[0].addressComponents[0].longName).isEqualTo(zipcode)


        val weatherData = homeViewModel.getWeatherResults(
            locationData.results[0].geometry?.location?.lat?.toInt().toString(),
            locationData.results[0].geometry?.location?.lng?.toInt().toString()
        )

        assertThat(weatherData.daily).isNotEmpty()
        assertThat(weatherData.hourly).isNotEmpty()
        assertThat(weatherData.current).isNotNull()
        assertThat(weatherData).isEqualTo(homeViewModel.currentWeatherResults.value)
    }

    @Test
    fun getCurrentLocationResultsFailsForMalformedZipcode() {
        val zipcode = "301t2"

        val locationData = homeViewModel.getLocationResults(zipcode)

        assertThat(locationData.results).isEmpty()
        assertThat(locationData.status).isEqualTo("ZERO_RESULTS")
        assertThat(locationData).isEqualTo(homeViewModel.currentLocationResults.value)
    }
}