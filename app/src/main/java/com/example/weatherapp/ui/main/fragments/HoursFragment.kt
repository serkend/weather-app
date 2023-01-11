package com.example.weatherapp.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.api.ApiHelper
import com.example.weatherapp.data.api.RetrofitClient
import com.example.weatherapp.data.model.converter_model.Forecastday
import com.example.weatherapp.data.model.converter_model.WeatherItem
import com.example.weatherapp.databinding.FragmentDayBinding
import com.example.weatherapp.databinding.FragmentHoursBinding
import com.example.weatherapp.ui.base.MainFactory
import com.example.weatherapp.ui.main.activities.MainActivity
import com.example.weatherapp.ui.main.adapters.HoursWeatherAdapter
import com.example.weatherapp.ui.main.adapters.WeatherAdapter
import com.example.weatherapp.ui.main.viewmodel.MainViewModel

class HoursFragment : Fragment() {

    lateinit var binding: FragmentHoursBinding
    lateinit var adapter: HoursWeatherAdapter
    lateinit var weatherItem: WeatherItem
    lateinit var dayItem:Forecastday

    val viewModel: MainViewModel by activityViewModels { MainFactory(ApiHelper(RetrofitClient.apiService)) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupObservers()

    }

    fun setupObservers() {
        viewModel.getWeather()
        viewModel.weatherItem.observe(viewLifecycleOwner) {
            weatherItem = it.body()!!
            dayItem = weatherItem.forecast.forecastday[0]
            setupUI()
        }
        viewModel.clickedDayItem.observe(viewLifecycleOwner) {
            dayItem = it
            setupUI()
        }
    }

    fun setupViewModel() {
//        viewModel = ViewModelProvider(
//            activity as MainActivity,
//            MainFactory(ApiHelper(RetrofitClient.apiService))
//        ).get(MainViewModel::class.java)
    }

    fun setupUI() {
        adapter = HoursWeatherAdapter(dayItem.hour)
        binding.rcHours.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rcHours.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}