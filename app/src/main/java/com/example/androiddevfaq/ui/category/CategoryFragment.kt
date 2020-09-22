package com.example.androiddevfaq.ui.category

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddevfaq.R
import com.example.androiddevfaq.api.FakeApiImpl
import com.example.androiddevfaq.databinding.FragmentCategoryBinding
import com.example.androiddevfaq.ui.base.BaseFragment
import com.example.androiddevfaq.ui.category.adapter.CategoryAdapter
import com.example.androiddevfaq.ui.category.event.GoToNextFragmentEvents
import com.example.androiddevfaq.ui.category.interactor.CategoryInteractor
import com.example.androiddevfaq.ui.category.viewmodel.CategoryFactory
import com.example.androiddevfaq.ui.category.viewmodel.CategoryViewModel
import com.example.androiddevfaq.ui.category.viewmodel.CategoryViewModelImpl
import com.example.androiddevfaq.ui.questionlist.QuestionListFragment
import kotlinx.android.synthetic.main.fragment_category.*

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
        initListeners()
        initObservers()
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(context)
        binding.categoryRecyclerView.layoutManager = layoutManager
        categoryAdapter = CategoryAdapter(categoryViewModel)
        binding.categoryRecyclerView.adapter = categoryAdapter
    }

    private fun initListeners() {
        category_swipe_refresh_layout.setOnRefreshListener {
            categoryViewModel.onSwipeRefreshLayout()
        }
    }

    private fun initObservers() {
        categoryViewModel.progressBarVisibility.observe(viewLifecycleOwner, {
            binding.categoryProgressBar.isVisible = it
        })

        categoryViewModel.recyclerViewVisibility.observe(viewLifecycleOwner, {
            binding.categoryRecyclerView.isVisible = it
        })

        categoryViewModel.setAdapterEvent.observe(viewLifecycleOwner, {
            categoryAdapter.setList(it.categoryList)
        })

        categoryViewModel.swipeRefreshVisibility.observe(viewLifecycleOwner, {
            binding.categorySwipeRefreshLayout.isRefreshing = it
        })

        categoryViewModel.goToNextFragmentEvents.observe(viewLifecycleOwner, {
            when(it) {
                is GoToNextFragmentEvents.QuestionList -> goToQuestionList(it.categoryID)
            }
        })
    }

    private fun goToQuestionList(categoryID: Int) {
        val bundle = QuestionListFragment.getBundle(categoryID)
        findNavController().navigate(R.id.action_categoryFragment_to_questionListFragment, bundle)
    }
}