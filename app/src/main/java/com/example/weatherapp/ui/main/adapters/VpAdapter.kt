package com.example.weatherapp.ui.main.adapters

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.ui.main.fragments.DayFragment
import com.example.weatherapp.ui.main.fragments.HoursFragment

class VpAdapter(fa:FragmentActivity, private val fragList:List<Fragment>) : FragmentStateAdapter(fa){
    override fun getItemCount(): Int {
        return fragList.size
    }

    override fun createFragment(position: Int): Fragment {
       return fragList[position]
    }
}