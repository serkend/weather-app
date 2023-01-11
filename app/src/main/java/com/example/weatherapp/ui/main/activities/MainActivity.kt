package com.example.weatherapp.ui.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.R
import com.example.weatherapp.ui.main.fragments.MainFragment
import com.example.weatherapp.ui.main.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        supportFragmentManager.beginTransaction()
            .replace(R.id.place_holder, MainFragment.newInstance())
            .commit()
    }
}