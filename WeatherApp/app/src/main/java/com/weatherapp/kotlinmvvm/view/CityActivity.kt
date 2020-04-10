package com.weatherapp.kotlinmvvm.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weatherapp.kotlinmvvm.R
import com.weatherapp.kotlinmvvm.model.City
import kotlinx.android.synthetic.main.activity_city.*
import java.io.InputStream
import java.lang.ref.WeakReference


class CityActivity : AppCompatActivity() {

    private lateinit var context: Context
    var citylist = listOf<City>()
    private lateinit var adapter: MultiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        context = this;
        val task = someTask(this)
        task.execute()


    }

    fun loadCities(context: Context) {
        val gson = Gson()
        val listPersonType = object : TypeToken<List<City>>() {}.type
        citylist = gson.fromJson(readJSONFromAsset(context), listPersonType)


    }


    fun readJSONFromAsset(context: Context): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = context.assets.open("citylist.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()


    }

    companion object {
        class someTask(context: CityActivity) : AsyncTask<Void, Void, String>() {
            private val activityReference: WeakReference<CityActivity> = WeakReference(context)

            override fun doInBackground(vararg params: Void?): String? {
                this.activityReference.get()?.loadCities(this.activityReference.get()!!.context)
                return null
            }

            override fun onPreExecute() {
                super.onPreExecute()
                this.activityReference.get()?.progressBar!!.visibility=View.VISIBLE


            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)

                this.activityReference.get()?.adapter =
                    MultiAdapter(this.activityReference.get()?.citylist!!)
                this.activityReference.get()
                    ?.recyclerView?.setAdapter(this.activityReference.get()?.adapter)

                this.activityReference.get()!!.progressBar.visibility=View.GONE
            }
        }
    }

    fun doneClick(view: View) {
        when (view?.id) {
            R.id.btn_done -> {
                var listSelected = adapter.getSelected();
                if (listSelected.size >= 3 && listSelected.size <= 7) {
                    setResult(
                        Activity.RESULT_OK,
                        Intent().apply { putExtra("result", adapter.getSelected()) })
                    onBackPressed()
                } else {
                    Toast.makeText(
                        this,
                        "Please select minimum 3 and max 7 cities",
                        Toast.LENGTH_LONG
                    ).show();
                }
            }

        }
    }
}