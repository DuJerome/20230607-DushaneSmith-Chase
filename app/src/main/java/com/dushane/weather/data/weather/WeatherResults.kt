package com.dushane.weather.data.weather

import com.google.gson.annotations.SerializedName


data class WeatherResults (

  @SerializedName("lat"             ) var lat            : Int?                = null,
  @SerializedName("lon"             ) var lon            : Int?                = null,
  @SerializedName("timezone"        ) var timezone       : String?             = null,
  @SerializedName("timezone_offset" ) var timezoneOffset : Int?                = null,
  @SerializedName("current"         ) var current        : Current?            = Current(),
  @SerializedName("minutely"        ) var minutely       : List<Minutely> = listOf(),
  @SerializedName("hourly"          ) var hourly         : List<Hourly>   = listOf(),
  @SerializedName("daily"           ) var daily          : List<Daily>    = listOf()

)