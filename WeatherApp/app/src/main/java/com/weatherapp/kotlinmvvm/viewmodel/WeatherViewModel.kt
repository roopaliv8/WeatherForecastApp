package com.weatherapp.kotlinmvvm.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weatherapp.kotlinmvvm.data.OperationCallback
import com.weatherapp.kotlinmvvm.model.City
import com.weatherapp.kotlinmvvm.model.WearherDataSource
import com.weatherapp.kotlinmvvm.model.WetherData
import java.io.InputStream

class WeatherViewModel(private val repository: WearherDataSource):ViewModel() {

    private val _weathers = MutableLiveData<WetherData>().apply { value = WetherData() }
    val weathers: LiveData<WetherData> = _weathers

    private val _isViewLoading=MutableLiveData<Boolean>()
    val isViewLoading:LiveData<Boolean> = _isViewLoading

    private val _onMessageError=MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    private val _isEmptyList=MutableLiveData<Boolean>()
    val isEmptyList:LiveData<Boolean> = _isEmptyList


    private val _cities = MutableLiveData<List<City>>().apply { value = emptyList() }
    val cities: LiveData<List<City>> = _cities
    /*
    If you require that the data be loaded only once, you can consider calling the method
    "loadData()" on constructor. Also, if you rotate the screen, the service will not be called.

    init {
        //loadData()
    }
     */

    fun loadData(id:String){
        _isViewLoading.postValue(true)
        repository.retrieveWeather(id,object:OperationCallback<WetherData>{
            override fun onSuccess(data: List<WetherData>?) {
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue( error)
            }

            override fun onSuccess(data: WetherData) {
                _isViewLoading.postValue(false)

                if(data.getList()?.isEmpty()!!){
                    _isEmptyList.postValue(true)
                }else{
                    _weathers.value= data
                }
            }
        })
    }

    fun loadforecastData(q:String){
        _isViewLoading.postValue(true)
        repository.retrieveForecast(q,object:OperationCallback<WetherData>{
            override fun onSuccess(data: List<WetherData>?) {
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue( error)
            }

            override fun onSuccess(data: WetherData) {
                _isViewLoading.postValue(false)

                if(data.getList()?.isEmpty()!!){
                    _isEmptyList.postValue(true)
                }else{
                    _weathers.value= data
                }
            }
        })
    }

}