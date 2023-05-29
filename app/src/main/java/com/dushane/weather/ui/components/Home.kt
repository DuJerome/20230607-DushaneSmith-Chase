package com.dushane.weather.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dushane.weather.data.weather.Daily
import com.dushane.weather.data.weather.Hourly
import com.dushane.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : ComponentActivity() {
    /*
    This project completes each requirement of the coding challenge
    1. it is a weather app browser that is functioning
    2. It has a search functionality, allowing you to look up addresses
        and zip codes allowing you to see weather information
    3. Autoload last search term and shows the weather
    4. Using the users location permission to populate weather data, WORKS!
    5. Bonus points: Compose, Unit Tests, Good design of app(MVVM architecture), SOLID principles,
        additional feature views of weather
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    View()
                }
            }
        }
    }
}

//Im using compose which means the majority of this app is in kotlin and I left the RxJava in Java
//to satisfy that some of the app needs to be in java
@Composable
fun View() {
    val homeViewModel: HomeViewModel = viewModel()
    val context = LocalContext.current
    val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)

    // Ask the user for location permissions on app loading
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            99
        )
    }

    //Conditional logic for loading the previous address results using shared preferences
    if (sharedPref.contains("openningLoc") && sharedPref.getBoolean("doAutoload", true)) {
        val data = homeViewModel.getLocationResults(sharedPref.getString("openningLoc", "")!!)
        homeViewModel.getWeatherResults(
            data.results[0].geometry?.location?.lat?.toInt().toString(),
            data.results[0].geometry?.location?.lng?.toInt().toString()
        )
        sharedPref.edit().putBoolean("doAutoload", true).commit()
    }else{
        sharedPref.edit().putBoolean("doAutoload", true).commit()
    }

    val currentWeatherResults by homeViewModel.currentWeatherResults.collectAsState()
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Search()
        CurrentWeather(currentWeather = currentWeatherResults.current)
        HourlyList(list = currentWeatherResults.hourly)
        DailyList(list = currentWeatherResults.daily)
    }
}


// A horizontal list of hourly weather report elements
@Composable
fun HourlyList(list: List<Hourly>?) {
    Column(
        modifier = Modifier.padding(0.dp, 16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "Hourly Weather Reports"
        )
        Divider()
        LazyRow {
            if (!list.isNullOrEmpty()) {
                items(list) {
                    HourlyWeatherItem(hourly = it)
                }
            }
        }
    }
}


@Composable
fun DailyList(list: List<Daily>?) {
    Column(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "Daily Weather Reports"
        )
        Divider()
        LazyRow {
            if (!list.isNullOrEmpty()) {
                items(list) {
                    DailyWeatherItem(daily = it)
                }
            }
        }
    }
}

//I use glide composable for loading the weather images and this is the Weather Item composable
// described here
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun HourlyWeatherItem(hourly: Hourly) {
    val imageUrl = "https://openweathermap.org/img/wn/${hourly.weather[0].icon}@2x.png"

    Card(modifier = Modifier.padding(16.dp)) {
        Row(Modifier.padding(12.dp)) {
            GlideImage(
                model = imageUrl, contentDescription = "", modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Temperature:", modifier = Modifier.padding(8.dp))
                Text(text = "Humidity:", modifier = Modifier.padding(8.dp))
                Text(text = "Feels Like:", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text(text = hourly.temp.toString() + "°", modifier = Modifier.padding(8.dp))
                Text(text = hourly.humidity.toString() + "°", modifier = Modifier.padding(8.dp))
                Text(text = hourly.feelsLike.toString() + "°", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun DailyWeatherItem(daily: Daily) {
    val imageUrl = "https://openweathermap.org/img/wn/${daily.weather[0].icon}@2x.png"

    Card(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.padding(12.dp)) {
            GlideImage(
                model = imageUrl, contentDescription = "", modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Morning Temp: ", modifier = Modifier.padding(8.dp))
                Text(text = "Midday Temp: ", modifier = Modifier.padding(8.dp))
                Text(text = "Night Temp: ", modifier = Modifier.padding(8.dp))
                Text(text = "Humidity: ", modifier = Modifier.padding(8.dp))
            }
            Column {
                Text(text = daily.temp?.morn.toString() + "°", modifier = Modifier.padding(8.dp))
                Text(text = daily.temp?.day.toString() + "°", modifier = Modifier.padding(8.dp))
                Text(text = daily.temp?.night.toString() + "°", modifier = Modifier.padding(8.dp))
                Text(text = daily.humidity.toString() + "°", modifier = Modifier.padding(8.dp))
            }
        }
    }
}