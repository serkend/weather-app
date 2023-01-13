package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.converter_model.WeatherItem
import retrofit2.Response

class ApiHelper(private val apiService: ApiService) {

    suspend fun getWeather(city : String): Response<WeatherItem> {
        return apiService.getWeather(city)
    }
}