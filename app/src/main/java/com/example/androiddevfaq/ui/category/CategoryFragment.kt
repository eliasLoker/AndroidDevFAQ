package com.example.androiddevfaq.ui.category

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddevfaq.R
import com.example.androiddevfaq.api.FakeApiImpl
import com.example.androiddevfaq.databinding.FragmentCategoryBinding
import com.example.androiddevfaq.ui.BaseFragment
import com.example.androiddevfaq.ui.category.adapter.CategoryAdapter
import com.example.androiddevfaq.ui.category.interactor.CategoryInteractor
import com.example.androiddevfaq.ui.category.viewmodel.CategoryFactory
import com.example.androiddevfaq.ui.category.viewmodel.CategoryViewModel
import com.example.androiddevfaq.ui.category.viewmodel.CategoryViewModelImpl

class CategoryFragment(
    layoutID: Int = R.layout.fragment_category
) : BaseFragment<FragmentCategoryBinding>(layoutID, FragmentCategoryBinding::inflate) {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val api = FakeApiImpl()
        val interactor = CategoryInteractor(api)
        val factory = CategoryFactory(interactor)
        categoryViewModel =
            ViewModelProviders.of(this, factory).get(CategoryViewModelImpl::class.java)
        categoryViewModel.onActivityCreated()
        initRecycler()
        initObservers()
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(context)
        binding.categoryRecyclerView.layoutManager = layoutManager
        categoryAdapter = CategoryAdapter()
        binding.categoryRecyclerView.adapter = categoryAdapter
    }

    private fun initObservers() {
        categoryViewModel.progressBarVisibility.observe(viewLifecycleOwner, {
            binding.categoryProgressBar.isVisible = it
        })

        categoryViewModel.setAdapterEvent.observe(viewLifecycleOwner, {
            categoryAdapter.setList(it.categoryList)
        })
    }
}