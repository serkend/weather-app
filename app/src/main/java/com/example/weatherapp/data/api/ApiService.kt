package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.converter_model.WeatherItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("forecast.json?key=b89e76a64a8c4610aad174842230901&q=London&days=7&aqi=no&alerts=no")
    suspend fun getWeather() : Response<WeatherItem>
}