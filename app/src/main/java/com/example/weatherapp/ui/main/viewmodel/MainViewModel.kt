package com.example.weatherapp.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.converter_model.Forecastday
import com.example.weatherapp.data.model.converter_model.WeatherItem
import com.example.weatherapp.data.repository.MainRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(val mainRepository: MainRepository) : ViewModel() {

    var weatherItem: MutableLiveData<Response<WeatherItem>> = MutableLiveData()
    var clickedDayItem: MutableLiveData<Forecastday> = MutableLiveData()

    fun getWeather(city: String) {
        viewModelScope.launch {
            weatherItem.value = mainRepository.getWeather(city)

        }
    }
}