package com.weatherapp.kotlinmvvm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Main {

    @SerializedName("temp")
    @Expose
    var temp: Double? = null
    @SerializedName("feels_like")
    @Expose
    var feelsLike: Double? = null
    @SerializedName("temp_min")
    @Expose
    var tempMin: Double? = null
    @SerializedName("temp_max")
    @Expose
    var tempMax: Double? = null
    @SerializedName("pressure")
    @Expose
    var pressure: Int? = null
    @SerializedName("humidity")
    @Expose
    var humidity: Int? = null
    @SerializedName("sea_level")
    @Expose
    var seaLevel: Int? = null
    @SerializedName("grnd_level")
    @Expose
    var grndLevel: Int? = null

}