package com.dushane.weather.data.location

import com.google.gson.annotations.SerializedName


data class LocationResults (

  @SerializedName("results" ) var results : ArrayList<Results> = arrayListOf(),
  @SerializedName("status"  ) var status  : String?            = null

)