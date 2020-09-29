package com.example.androiddevfaq.ui.maintmp

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentMainTmpBinding
import com.example.androiddevfaq.ui.base.BaseFragment

class MainTmpFragment(
    layoutID: Int = R.layout.fragment_main_tmp
) : BaseFragment<FragmentMainTmpBinding>(layoutID, FragmentMainTmpBinding::inflate) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.goToCategoriesButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainTmpFragment_to_categoryFragment)
        }

        binding.goToFavourites.setOnClickListener {
            findNavController().navigate(R.id.action_mainTmpFragment_to_favouritesFragment)
        }
    }
}