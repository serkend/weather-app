package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.ApiHelper
import com.example.weatherapp.data.model.converter_model.WeatherItem
import retrofit2.Response

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getWeather(): Response<WeatherItem> = apiHelper.getWeather()
}