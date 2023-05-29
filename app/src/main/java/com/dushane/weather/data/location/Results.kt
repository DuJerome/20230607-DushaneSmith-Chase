package com.dushane.weather.data.location

import com.google.gson.annotations.SerializedName


data class Results (

    @SerializedName("address_components"  ) var addressComponents  : ArrayList<AddressComponents> = arrayListOf(),
    @SerializedName("formatted_address"   ) var formattedAddress   : String?                      = null,
    @SerializedName("geometry"            ) var geometry           : Geometry?                    = Geometry(),
    @SerializedName("place_id"            ) var placeId            : String?                      = null,
    @SerializedName("postcode_localities" ) var postcodeLocalities : ArrayList<String>            = arrayListOf(),
    @SerializedName("types"               ) var types              : ArrayList<String>            = arrayListOf()

)