package com.example.weatherapp.data.model.converter_model

data class WeatherItem(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)