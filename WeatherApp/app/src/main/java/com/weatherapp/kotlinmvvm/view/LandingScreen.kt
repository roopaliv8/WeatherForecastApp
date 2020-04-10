package com.weatherapp.kotlinmvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.weatherapp.kotlinmvvm.R

class LandingScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landingscreen)


    }

    fun landingClick(view: View) {
        when (view?.id) {
            R.id.btn_step1 -> {
                val intent = Intent(this, WeatherActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_step2 -> {
                val intent = Intent(this, ForecastActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

}