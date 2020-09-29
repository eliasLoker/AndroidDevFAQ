package com.example.androiddevfaq.ui.category

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
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
import kotlinx.android.synthetic.main.toolbar_question_list.view.*

class CategoryFragment(
    layoutID: Int = R.layout.fragment_category
) : BaseFragment<FragmentCategoryBinding>(layoutID, FragmentCategoryBinding::inflate) {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        val api = FakeApiImpl()
        val interactor = CategoryInteractor(App.getApi())
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

        binding.categorySwipeRefreshLayout.setOnRefreshListener {
            categoryViewModel.onSwipeRefreshLayout()
        }

        binding.sortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoryViewModel.onSortSpinnerItemSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun initObservers() {

        categoryViewModel.initToolbar.observe(viewLifecycleOwner, {
            binding.categoryToolbar.toolbar.apply {
                title = "AndroidDevFAQ"
                subtitle = "Категории"
            }
        })

        categoryViewModel.initSortSpinner.observe(viewLifecycleOwner, {
            val sortOptions = arrayOf("По умолчанию", "По кол-ву вопросов")
            initAdapter(sortOptions)
        })

        categoryViewModel.progressBarVisibility.observe(viewLifecycleOwner, {
            binding.categoryProgressBar.isVisible = it
        })

        categoryViewModel.recyclerViewVisibility.observe(viewLifecycleOwner, {
            binding.categoryRecyclerView.isVisible = it
        })

        categoryViewModel.setAdapterEvent.observe(viewLifecycleOwner, {
            categoryAdapter.setList(it.categoryItemList)
        })

        categoryViewModel.swipeRefreshVisibility.observe(viewLifecycleOwner, {
            binding.categorySwipeRefreshLayout.isRefreshing = it
        })

        categoryViewModel.goToNextFragmentEvents.observe(viewLifecycleOwner, {
            when (it) {
                is GoToNextFragmentEvents.QuestionList -> goToQuestionList(it.categoryID, it.categoryName)
            }
        })

        categoryViewModel.sortSpinnerVisibility.observe(viewLifecycleOwner, {
            binding.sortSpinner.isVisible = it
        })
    }

    private fun initAdapter(optionsArray: Array<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            optionsArray
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.sortSpinner.adapter = adapter
    }

    private fun goToQuestionList(categoryID: Int, categoryName: String) {
        val bundle = QuestionListFragment.getBundle(categoryID, categoryName)
        findNavController().navigate(R.id.action_categoryFragment_to_questionListFragment, bundle)
    }

    companion object {

        @JvmStatic
        fun newInstance() = CategoryFragment()
    }
}