package com.example.livioreinoso1165606.finalexamreview.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.livioreinoso1165606.finalexamreview.R
import com.example.livioreinoso1165606.finalexamreview.model.WeatherEntity

class WeatherListAdapter(private val weatherList: List<WeatherEntity>) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.cityName)
        val feels = itemView.findViewById<TextView>(R.id.feelsLike)
        val temp = itemView.findViewById<TextView>(R.id.tempC)
        val condition = itemView.findViewById<TextView>(R.id.condition)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.count()
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val current = weatherList[position]
        holder.name.text = current.city
        holder.temp.text = current.temp_c.toString()
        holder.feels.text = current.feels_like.toString()
        holder.condition.text = current.condition
    }
}
