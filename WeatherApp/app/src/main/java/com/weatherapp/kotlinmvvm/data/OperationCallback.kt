package com.weatherapp.kotlinmvvm.data

import com.weatherapp.kotlinmvvm.model.WetherData

interface OperationCallback<T> {
    fun onSuccess(data:List<T>?)
    fun onError(error:String?)
    fun onSuccess(data:WetherData)

}