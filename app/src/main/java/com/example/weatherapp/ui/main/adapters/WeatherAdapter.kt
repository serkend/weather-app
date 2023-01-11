package com.example.weatherapp.ui.main.adapters

import android.icu.number.NumberFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.converter_model.Forecastday
import com.example.weatherapp.data.model.converter_model.WeatherItem
import com.example.weatherapp.databinding.WeatherListItemBinding
import com.squareup.picasso.Picasso

class WeatherAdapter(var weatherItem: WeatherItem, private var listener:Listener) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var dayItemList: List<Forecastday> = weatherItem.forecast.forecastday

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = WeatherListItemBinding.bind(view)

        fun bindItem(dayItem: Forecastday) = with(binding) {
            tvDateItem.text = dayItem.date
            tvStateItem.text = dayItem.day.condition.text
            tvDegreesItem.text = dayItem.day.avgtemp_c.toString() + "°C"
            // Picasso.get().isLoggingEnabled = true
            Picasso.get().load("https://${dayItem.day.condition.icon}").into(ivState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.weather_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindItem(dayItemList[position])
        holder.itemView.setOnClickListener {
            listener.onClick(dayItemList[position])
        }

    }

    override fun getItemCount(): Int {
        return dayItemList.size
    }

    interface Listener {
        fun onClick(dayItem: Forecastday)
    }

}