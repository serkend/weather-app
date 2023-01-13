package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.converter_model.WeatherItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("forecast.json?key=b89e76a64a8c4610aad174842230901&days=7&aqi=no&alerts=no")
    suspend fun getWeather(@Query("q") city : String) : Response<WeatherItem>
}