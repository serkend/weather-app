package com.example.weatherapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.converter_model.Forecastday
import com.example.weatherapp.data.model.converter_model.Hour
import com.example.weatherapp.data.model.converter_model.WeatherItem
import com.example.weatherapp.databinding.WeatherListItemBinding
import com.squareup.picasso.Picasso


class HoursWeatherAdapter(private var hoursList: List<Hour>) :
    RecyclerView.Adapter<HoursWeatherAdapter.HoursWeatherViewHolder>() {

    inner class HoursWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = WeatherListItemBinding.bind(view)

        fun bindItem(hourItem: Hour) = with(binding) {
            tvDateItem.text = hourItem.time
            tvStateItem.text = hourItem.condition.text
            tvDegreesItem.text = hourItem.temp_c.toString()
            // Picasso.get().isLoggingEnabled = true
            Picasso.get().load("https://${hourItem.condition.icon}").into(ivState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursWeatherViewHolder {
        return HoursWeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_list_item, parent,false))
    }

    override fun onBindViewHolder(holder: HoursWeatherViewHolder, position: Int) {
        holder.bindItem(hoursList[position])
    }

    override fun getItemCount(): Int {
        return hoursList.size
    }

}