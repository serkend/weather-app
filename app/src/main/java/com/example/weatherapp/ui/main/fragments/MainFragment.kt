package com.example.weatherapp.ui.main.fragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.api.ApiHelper
import com.example.weatherapp.data.api.RetrofitClient
import com.example.weatherapp.data.model.converter_model.Forecastday
import com.example.weatherapp.data.model.converter_model.WeatherItem
import com.example.weatherapp.databinding.FragmentDayBinding
import com.example.weatherapp.ui.main.adapters.VpAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.ui.base.MainFactory
import com.example.weatherapp.ui.main.activities.MainActivity
import com.example.weatherapp.ui.main.adapters.WeatherAdapter
import com.example.weatherapp.ui.main.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class MainFragment : Fragment() {
    lateinit var weatherItem: WeatherItem
    lateinit var dayItem: Forecastday
    val viewModel: MainViewModel by activityViewModels { MainFactory(ApiHelper(RetrofitClient.apiService)) }

    private val fragList = listOf(
        HoursFragment.newInstance(),
        DayFragment.newInstance()
    )

    private val tabList = listOf(
        "Hours",
        "Days"
    )

    lateinit var binding: FragmentMainBinding
    lateinit var pLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        init()
        setupViewModel()
        setupObservers()
    }

    private fun permissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(activity, "$it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun init() = with(binding) {
        val vpAdapter = VpAdapter(activity as FragmentActivity, fragList)
        vpView.adapter = vpAdapter

        TabLayoutMediator(tabLayout, vpView) { tab, p ->
            tab.text = tabList[p]
        }.attach()
    }

    fun setupObservers() {
        viewModel.getWeather()
        viewModel.weatherItem.observe(viewLifecycleOwner) {
            weatherItem = it.body()!!
            setupUI()
        }
        viewModel.clickedDayItem.observe(viewLifecycleOwner) {
            dayItem = it
            setupCurrentForecastUI()
        }
    }

    fun setupViewModel() {
//        viewModel = ViewModelProvider(
//            activity as MainActivity,
//            MainFactory(ApiHelper(RetrofitClient.apiService))
//        ).get(MainViewModel::class.java)
    }

    fun setupUI() = with(binding) {
        tvDate.text = weatherItem.current.last_updated
        tvCity.text = weatherItem.location.region
        Picasso.get().load("https://${weatherItem.current.condition.icon}").into(ivSunny)
        tvState.text = weatherItem.current.condition.text
        val min = weatherItem.forecast.forecastday[0].day.mintemp_c
        val max = weatherItem.forecast.forecastday[0].day.maxtemp_c
        tvMinMax.text = "${min}°C/${max}°C"
        tvDegrees.text = weatherItem.current.temp_c.toString() + "°C"
    }

    fun setupCurrentForecastUI() = with(binding) {
        tvDate.text = dayItem.date
        tvCity.text = weatherItem.location.region
        Picasso.get().load("https://${dayItem.day.condition.icon}").into(ivSunny)
        tvState.text = dayItem.day.condition.text
        val min = dayItem.day.mintemp_c
        val max = dayItem.day.maxtemp_c
        tvMinMax.text = "${min}°C/${max}°C"
        tvDegrees.text = dayItem.day.avgtemp_c.toString() + "°C"
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}