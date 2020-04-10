package com.weatherapp.kotlinmvvm.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.weatherapp.kotlinmvvm.R
import com.weatherapp.kotlinmvvm.di.Injection
import com.weatherapp.kotlinmvvm.model.City
import com.weatherapp.kotlinmvvm.model.WetherData
import com.weatherapp.kotlinmvvm.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.layout_error.*
import java.lang.Exception
import java.util.*

class ForecastActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: WeatherAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val MY_PERMISSIONS_REQUEST = 1;
    companion object {
        const val TAG = "CONSOLE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setTitle(getString(R.string.forecastdata))
        txtCity.visibility=View.GONE
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            setupUI()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST
            )
        }
    }
    //ui
    private fun setupUI() {


        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val cityName = addresses[0].locality
                    getSupportActionBar()?.setTitle(cityName+" "+ getString(R.string.forecastdata))


                    setupViewModel()

                    adapter = WeatherAdapter(viewModel.weathers.value?.getList() ?: emptyList())
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = adapter



                    viewModel.loadforecastData(cityName)

                }
            }


    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory()
        ).get(WeatherViewModel::class.java)
        //viewModel = ViewModelProvider(this,ViewModelFactory(Injection.providerRepository())).get(WeatherViewModel::class.java)
        //viewModel = ViewModelProviders.of(this,ViewModelFactory(Injection.providerRepository())).get(WeatherViewModel::class.java)
        viewModel.weathers.observe(this, renderWeather)

        viewModel.isViewLoading.observe(this, isViewLoadingObserver)
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.isEmptyList.observe(this, emptyListObserver)
    }

    //observers
    private val renderWeather = Observer<WetherData> {
        Log.v(TAG, "data updated $it")
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.GONE
        try {
            adapter.update(it.getList())
        }
        catch (e:Exception)
        {}

    }

    private val isViewLoadingObserver = Observer<Boolean> {
        Log.v(TAG, "isViewLoading $it")
        val visibility = if (it) View.VISIBLE else View.GONE
        progressBar.visibility = visibility
    }

    private val onMessageErrorObserver = Observer<Any> {
        Log.v(TAG, "onMessageError $it")
        layoutError.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        textViewError.text = "Error $it"
    }

    private val emptyListObserver = Observer<Boolean> {
        Log.v(TAG, "emptyListObserver $it")
        layoutEmpty.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            var dats = data?.getParcelableArrayListExtra<City>("result");
            var value = ""
            if (dats != null) {
                for (city in dats) {
                    value = value + city.id + ",";
                }
            }
            viewModel.loadData(value)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {

        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setupUI()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
