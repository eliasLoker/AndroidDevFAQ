package com.example.androiddevfaq.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentMainBinding
import com.example.androiddevfaq.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment(
    layoutID: Int = R.layout.fragment_main
) : BaseFragment<FragmentMainBinding>(layoutID, FragmentMainBinding::inflate) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = MainViewPagerAdapter(childFragmentManager, lifecycle)
        binding.mainViewPager.adapter = adapter
        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Not main"
            }
        }.attach()
    }

    companion object {

        @JvmStatic
        fun getBundle(param1: String, param2: String) = Bundle().apply {

        }
    }

}