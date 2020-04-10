package com.weatherapp.kotlinmvvm.model

import com.weatherapp.kotlinmvvm.data.OperationCallback

interface WearherDataSource {

    fun retrieveWeather(id: String,callback: OperationCallback<WetherData>)
    fun cancel()
    fun retrieveForecast(q: String, callback: OperationCallback<WetherData>)
}