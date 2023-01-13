package com.example.weatherapp.ui.main.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class MainFragment : Fragment() {
    private lateinit var fLocationClient: FusedLocationProviderClient
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

    override fun onResume() {
        super.onResume()
        checkLocation()
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

        fLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        ibSync.setOnClickListener {
            getLocation()
            tabLayout.selectTab(tabLayout.getTabAt(0))
        }

        ibSearch.setOnClickListener {
            runSearchCityDialog()
        }
    }

    private fun checkGPSEnabled(): Boolean {
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun checkLocation() {
        if (!checkGPSEnabled()) {
//            Toast.makeText(activity, "GPS is disabled. Turn it on.", Toast.LENGTH_LONG).show()
            runDialogForGPSEnabled()
        } else {
            getLocation()
        }
    }

    private fun getLocation() {

        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            fLocationClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
                .addOnCompleteListener {

                    Log.d(
                        "MyLog: ",
                        "After syncing location: ${viewModel.getWeather("${it.result.latitude},${it.result.longitude}")}"
                    )
                }
        }
    }

    fun runDialogForGPSEnabled() {
        val builder = AlertDialog.Builder(activity)
        val dialog = builder.create()
        dialog.setTitle("Do you want to enable GPS?")
        dialog.setMessage("GPS is disabled. GPS should be enabled for showing weather.")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun runSearchCityDialog() {
        val builder = AlertDialog.Builder(activity)
        val dialog = builder.create()
        val etCity = EditText(activity)
        dialog.setView(etCity)
        dialog.setTitle("Enter city:")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            viewModel.getWeather(etCity.text.toString())
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun setupObservers() {
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
        tvCity.text = "${weatherItem.location.name}, ${weatherItem.location.country}"
        Picasso.get().load("https://${weatherItem.current.condition.icon}").into(ivSunny)
        tvState.text = weatherItem.current.condition.text
        val min = weatherItem.forecast.forecastday[0].day.mintemp_c
        val max = weatherItem.forecast.forecastday[0].day.maxtemp_c
        tvMinMax.text = "${min}°C/${max}°C"
        tvDegrees.text = weatherItem.current.temp_c.toString() + "°C"
    }

    fun setupCurrentForecastUI() = with(binding) {
        tvDate.text = dayItem.date
        tvCity.text = "${weatherItem.location.name}, ${weatherItem.location.country}"
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