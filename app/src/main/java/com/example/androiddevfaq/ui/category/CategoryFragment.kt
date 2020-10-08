package com.example.androiddevfaq.ui.category

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentCategoryBinding
import com.example.androiddevfaq.base.BaseFragment
import com.example.androiddevfaq.base.observe
import com.example.androiddevfaq.ui.category.adapter.CategoryAdapter
import com.example.androiddevfaq.ui.category.event.CategoryNavigationEvents
import com.example.androiddevfaq.ui.category.interactor.CategoryInteractor
import com.example.androiddevfaq.ui.category.viewmodel.CategoryFactory
import com.example.androiddevfaq.ui.category.viewmodel.CategoryViewModel
import com.example.androiddevfaq.ui.questionlist.QuestionListFragment
import com.example.androiddevfaq.utils.navigate
//import com.example.androiddevfaq.ui.questionlist.QuestionListFragmentOld
import com.example.androiddevfaq.utils.onItemSelected

class CategoryFragment(
    layoutID: Int = R.layout.fragment_category
) : BaseFragment<FragmentCategoryBinding>(layoutID, FragmentCategoryBinding::inflate) {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    private val stateObserver = Observer<CategoryViewModel.ViewState> {
        binding.categoryProgressBar.isVisible = it.progressBarVisibility
        binding.categoryRecyclerView.isVisible = it.categoryRecyclerVisibility
        binding.categorySwipeRefreshLayout.isRefreshing = it.swipeRefreshVisibility
        binding.sortSpinner.isVisible = it.sortSpinnerVisibility
        categoryAdapter.setList(it.categoryList)
        binding.errorTextView.isVisible = it.errorTextViewVisibility
//        //TODO("Посмотреть, как прикольно сэтить ресайклер в AlbumAdapter")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val interactor = CategoryInteractor(App.getApi())
        val factory = CategoryFactory(interactor)
        categoryViewModel =
            ViewModelProviders.of(this, factory).get(CategoryViewModel::class.java)

        observe(categoryViewModel.stateLiveData, stateObserver)

        categoryViewModel.initToolbar.observe(viewLifecycleOwner, {
            binding.categoryToolbar.toolbar.apply {
                title = "AndroidDevFAQ"
                subtitle = "Категории"
            }
        })

        categoryViewModel.onActivityCreated(savedInstanceState == null)

        categoryViewModel.categoryNavigationEvents.observe(viewLifecycleOwner, {
            when (it) {
                is CategoryNavigationEvents.GoToQuestionList -> goToQuestionList(
                    it.categoryID,
                    it.categoryName
                )
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRecyclerView.layoutManager = layoutManager
        categoryAdapter = CategoryAdapter(categoryViewModel)
        binding.categoryRecyclerView.adapter = categoryAdapter

        val sortOptions = arrayOf("По умолчанию", "По кол-ву вопросов")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sortOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.sortSpinner.adapter = adapter

        binding.categorySwipeRefreshLayout.setOnRefreshListener {
            binding.categorySwipeRefreshLayout.isRefreshing = true
            categoryViewModel.onSwipeRefreshLayout()
        }

        binding.sortSpinner.onItemSelected {
            categoryViewModel.onSortSpinnerItemSelected(it)
        }
    }

    private fun goToQuestionList(categoryID: Int, categoryName: String) {
//        val bundle = QuestionListFragment.getBundle(categoryID, categoryName)
//        findNavController().navigate(
//            R.id.action_categoryFragmentNew_to_questionListFragment2,
//            bundle
//        )
        navigate(
            R.id.action_categoryFragmentNew_to_questionListFragment2,
            QuestionListFragment.getBundle(categoryID, categoryName)
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() = CategoryFragment()
    }
}