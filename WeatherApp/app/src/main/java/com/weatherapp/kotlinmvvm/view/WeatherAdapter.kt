package com.weatherapp.kotlinmvvm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weatherapp.kotlinmvvm.R

class WeatherAdapter(private var list:List<com.weatherapp.kotlinmvvm.model.List>):RecyclerView.Adapter<WeatherAdapter.MViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_weather, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val weathers= list[position]

        //render
        vh.textViewTemp.text= "Min Temp:- "+ (weathers.main?.tempMin ?: "") +"\n Max Temp:- "+ (weathers.main?.tempMax
            ?: "")
        vh.textViewWeather.text= "weather:- "+weathers.weather?.get(0)?.description.toString()
        vh.textViewWind.text= "Speed:- "+weathers.wind?.speed.toString()

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(data:List<com.weatherapp.kotlinmvvm.model.List>?){
        if (data != null) {
            list= data
        }
        notifyDataSetChanged()
    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewTemp: TextView = view.findViewById(R.id.texttemp)

        val textViewWeather:TextView= view.findViewById(R.id.textweather)


        val textViewWind:TextView= view.findViewById(R.id.textwind)
    }
}