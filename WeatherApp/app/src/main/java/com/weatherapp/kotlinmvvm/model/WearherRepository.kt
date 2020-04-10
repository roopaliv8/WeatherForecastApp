package com.weatherapp.kotlinmvvm.model

import android.util.Log
import com.weatherapp.kotlinmvvm.Utility.Constants
import com.weatherapp.kotlinmvvm.data.ApiClient
import com.weatherapp.kotlinmvvm.data.OperationCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG="CONSOLE"

class WearherRepository:WearherDataSource {

    private var call:Call<WetherData>?=null

    override fun retrieveWeather(id: String,callback: OperationCallback<WetherData>) {
        call=ApiClient.build()?.weatherdata(id,Constants.APPID)
        call?.enqueue(object :Callback<WetherData>{
            override fun onFailure(call: Call<WetherData>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(call: Call<WetherData>, response: Response<WetherData>) {
                response?.body()?.let {
                    if(response.isSuccessful){
                        Log.v(TAG, "data ${it.getList()}")
                        callback.onSuccess(it)
                    }else{
                        callback.onError("it.msg")
                    }
                }
            }
        })
    }

    override fun retrieveForecast(q: String,callback: OperationCallback<WetherData>) {
        call=ApiClient.build()?.forecastdata(q,Constants.APPID)
        call?.enqueue(object :Callback<WetherData>{
            override fun onFailure(call: Call<WetherData>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(call: Call<WetherData>, response: Response<WetherData>) {
                response?.body()?.let {
                    if(response.isSuccessful){
                        Log.v(TAG, "data ${it.getList()}")
                        callback.onSuccess(it)
                    }else{
                        callback.onError("it.msg")
                    }
                }
            }
        })
    }
    override fun cancel() {
        call?.let {
            it.cancel()
        }
    }
}