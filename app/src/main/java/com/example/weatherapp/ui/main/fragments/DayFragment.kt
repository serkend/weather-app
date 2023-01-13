package com.example.weatherapp.ui.main.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class DayFragment : Fragment(), WeatherAdapter.Listener {
    private lateinit var fLocationClient: FusedLocationProviderClient

    lateinit var binding: FragmentDayBinding
    lateinit var adapter: WeatherAdapter
    lateinit var weatherItem:WeatherItem

    val viewModel: MainViewModel by activityViewModels { MainFactory(ApiHelper(RetrofitClient.apiService)) }

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
        fLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLocation()
    }

    fun setupObservers() {
        viewModel.weatherItem.observe(viewLifecycleOwner) {
            weatherItem = it.body()!!
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
        adapter = WeatherAdapter(weatherItem, this)
        binding.rcDay.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rcDay.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
    }

    private fun checkGPSEnabled(): Boolean {
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLocation() {
//        if(!checkGPSEnabled()) {
//            Toast.makeText(activity, "GPS is disabled. Turn it on.", Toast.LENGTH_LONG).show()
//            return
//        }
//
//        val ct = CancellationTokenSource()
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.d("MyLog: ", "Something went wrong! ")
//            return
//        }
//        fLocationClient
//            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
//            .addOnCompleteListener {
//
//                Log.d("MyLog: ", "After syncing location: ${viewModel.getWeather("${it.result.latitude},${it.result.longitude}")}")
//            }
    }

    companion object {

        @JvmStatic
        fun newInstance() = DayFragment()
    }

    override fun onClick(dayItem: Forecastday) {
        viewModel.clickedDayItem.value = dayItem
        Log.d("MyLog: ", "Clicked - $weatherItem")
    }
}