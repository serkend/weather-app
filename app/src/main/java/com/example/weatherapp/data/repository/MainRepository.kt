package com.example.weatherapp.data.repository

import android.util.Log
import com.example.weatherapp.data.api.ApiHelper
import com.example.weatherapp.data.model.converter_model.WeatherItem
import retrofit2.Response

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getWeather(city: String): Response<WeatherItem> {
        Log.d("MyLog: ", "GetWeather response city: $city")
        Log.d("MyLog: ", "GetWeather response: ${apiHelper.getWeather(city)}")

        return apiHelper.getWeather(city)
    }
}