package com.dushane.weather.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dushane.weather.R
import java.util.function.Consumer

@Composable
fun Search() {
    val homeViewModel: HomeViewModel = viewModel()
    val context = LocalContext.current
    val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)


    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(sharedPref.getString("openningLoc", "")!!))
    }

    Row() {
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText: TextFieldValue ->
                searchText = newText
            },
            modifier = Modifier
                .padding(16.dp)
                .width(325.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                )
            },
            trailingIcon = null,
            label = { Text("Search") },
            placeholder = { Text("Search..") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(
                onDone = {
                    //This  is wherre I viewModel data gets update and its new values enter the state
                    val locData = homeViewModel.getLocationResults(searchText.text)
                    if (!locData.results.isEmpty()) {
                        homeViewModel.getWeatherResults(
                            locData.results[0].geometry?.location?.lat?.toInt().toString(),
                            locData.results[0].geometry?.location?.lng?.toInt().toString()
                        )
                    }
                    // I use shared preference for proper rendering logic and initial app loading logic
                    sharedPref.edit().putString("openningLoc", searchText.text).commit()
                    sharedPref.edit().putBoolean("doAutoload", false).commit()
                },
            ),
            singleLine = true,
            enabled = true,
            readOnly = false,
            isError = false,
            textStyle = MaterialTheme.typography.bodyMedium
        )
        IconButton(
            onClick =
            {
                //Incase the user rejects the permission I ask them for it again before doing any
                // logic using location
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
                    Toast.makeText(context, "Wait a moment and press once more for weather data", Toast.LENGTH_LONG).show()
                } else {
                    var locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                    if (hasGps && hasNetwork){
                        locationManager.getCurrentLocation(
                            LocationManager.GPS_PROVIDER,
                            null,
                            context.mainExecutor,
                            Consumer {
                                //This is where the the function call is made and I update the state
                                homeViewModel.getWeatherResults(
                                    it.latitude.toInt().toString(),
                                    it.longitude.toInt().toString()
                                )
                            }
                        )
                        sharedPref.edit().putBoolean("doAutoload", false).commit()
                    }
                }
            },
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_my_location_24),
                null,
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
            )
        }
    }
}