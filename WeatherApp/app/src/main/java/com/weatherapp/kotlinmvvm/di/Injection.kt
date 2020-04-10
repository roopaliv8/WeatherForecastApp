package com.weatherapp.kotlinmvvm.di

import androidx.lifecycle.ViewModelProvider
import com.weatherapp.kotlinmvvm.model.WearherDataSource
import com.weatherapp.kotlinmvvm.model.WearherRepository
import com.weatherapp.kotlinmvvm.viewmodel.ViewModelFactory

object Injection {

    private val WEARHER_DATA_SOURCE:WearherDataSource = WearherRepository()
    private val weatherViewModelFactory = ViewModelFactory(WEARHER_DATA_SOURCE)

    fun providerRepository():WearherDataSource{
        return WEARHER_DATA_SOURCE
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory{
        return weatherViewModelFactory
    }
}