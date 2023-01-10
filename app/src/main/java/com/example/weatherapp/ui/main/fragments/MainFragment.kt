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
import com.example.weatherapp.R
import com.example.weatherapp.ui.main.adapters.VpAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
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

    companion object {
        fun newInstance() = MainFragment()
    }
}