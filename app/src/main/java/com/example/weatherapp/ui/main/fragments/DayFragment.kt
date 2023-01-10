package com.example.weatherapp.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.data.api.ApiHelper
import com.example.weatherapp.data.api.RetrofitClient
import com.example.weatherapp.data.model.converter_model.Forecastday
import com.example.weatherapp.data.model.converter_model.WeatherItem
import com.example.weatherapp.ui.main.adapters.WeatherAdapter
import com.example.weatherapp.databinding.FragmentDayBinding
import com.example.weatherapp.ui.base.MainFactory
import com.example.weatherapp.ui.main.activities.MainActivity
import com.example.weatherapp.ui.main.viewmodel.MainViewModel

class DayFragment : Fragment() {
    lateinit var binding: FragmentDayBinding
    lateinit var adapter: WeatherAdapter
    lateinit var weatherItem:WeatherItem

    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
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
            setupUI()
        }
    }

    fun setupViewModel() {
        viewModel = ViewModelProvider(
            activity as MainActivity,
            MainFactory(ApiHelper(RetrofitClient.apiService))
        ).get(MainViewModel::class.java)
    }

    fun setupUI() {
        adapter = WeatherAdapter(weatherItem)
        binding.rcDay.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rcDay.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
    }

    companion object {

        @JvmStatic
        fun newInstance() = DayFragment()
    }
}