package com.weatherapp.kotlinmvvm.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.weatherapp.kotlinmvvm.R
import com.weatherapp.kotlinmvvm.di.Injection
import com.weatherapp.kotlinmvvm.model.City
import com.weatherapp.kotlinmvvm.model.WetherData
import com.weatherapp.kotlinmvvm.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.layout_error.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: WeatherAdapter

    companion object {
        const val TAG = "CONSOLE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setTitle(getString(R.string.weatherdata))
        setupViewModel()
        setupUI()
    }

    //ui
    private fun setupUI() {
        adapter = WeatherAdapter(viewModel.weathers.value?.getList() ?: emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        txtCity.setOnClickListener { askUserToSelectCities() }
    }

    private fun askUserToSelectCities() {
        val intent = Intent(this, CityActivity::class.java)
        startActivityForResult(intent, 0)
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
        adapter.update(it.getList())
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
            var cities=""
            if (dats != null) {
                for (city in dats) {
                    value = value + city.id + ","
                    cities=cities+city.name+", "
                }
            }
            txtSelected.text="Selected Cities:-\n"+cities
            viewModel.loadData(value)
        }
    }

}
