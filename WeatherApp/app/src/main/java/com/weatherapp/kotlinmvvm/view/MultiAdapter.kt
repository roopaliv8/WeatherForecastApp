package com.weatherapp.kotlinmvvm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weatherapp.kotlinmvvm.R
import com.weatherapp.kotlinmvvm.model.City






class MultiAdapter(private var list:List<City>):RecyclerView.Adapter<MultiAdapter.MViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val weathers= list[position]
        vh.bind(weathers);

    }

    override fun getItemCount(): Int {
        return list.size
    }



    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView = view.findViewById(R.id.textView)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        fun bind(city: City) {
            imageView.setVisibility(if (city.isChecked) View.VISIBLE else View.GONE)
            textViewName.setText(city.name)

            itemView.setOnClickListener({
                city.isChecked=!city.isChecked
                imageView.setVisibility(if (city.isChecked) View.VISIBLE else View.GONE)

            })
        }
    }

    fun getSelected(): ArrayList<City> {
        val selected = arrayListOf<City>()
        for (i in 0 until list.size) {
            if (list.get(i).isChecked) {
                selected.add(list.get(i))
            }
        }
        return selected
    }
}