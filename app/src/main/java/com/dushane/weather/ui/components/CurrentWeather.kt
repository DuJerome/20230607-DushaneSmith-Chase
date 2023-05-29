package com.dushane.weather.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dushane.weather.data.weather.Current

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CurrentWeather(currentWeather: Current?){
    if (currentWeather == null || currentWeather.weather.isEmpty()) return
    val imageUrl = "https://openweathermap.org/img/wn/${currentWeather.weather.get(0).icon}@2x.png"

    Text(
        "Current Weather",
        modifier = Modifier
            .padding(8.dp),
    )
    Divider()
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
                Text(text = currentWeather.temp.toString() + "°", modifier = Modifier.padding(8.dp))
                Text(text = currentWeather.humidity.toString() + "°", modifier = Modifier.padding(8.dp))
                Text(text = currentWeather.feelsLike.toString() + "°", modifier = Modifier.padding(8.dp))
            }
        }
    }
}