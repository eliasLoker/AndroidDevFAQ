package com.example.androiddevfaq.ui.category

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import com.example.androiddevfaq.ui.questions.QuestionsFragment
import com.example.androiddevfaq.utils.AdapterItemDecorator
import com.example.androiddevfaq.utils.navigate
import com.example.androiddevfaq.utils.onItemSelected

class CategoryFragment(
    layoutID: Int = R.layout.fragment_category
) : BaseFragment<FragmentCategoryBinding>(layoutID, FragmentCategoryBinding::inflate) {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    private val stateObserver = Observer<CategoryViewModel.ViewState> {
        binding.apply {
            mainProgressBar.isVisible = it.progressBarVisibility
            recyclerView.isVisible = it.categoryRecyclerVisibility
            swipeRefreshLayout.isRefreshing = it.swipeRefreshVisibility
            sortSpinner.isVisible = it.sortSpinnerVisibility
            errorTextView.isVisible = it.errorTextViewVisibility
            errorTextView.text = it.errorMessage ?: requireContext().getText(R.string.categories_error)
            emptyTextView.isVisible = it.emptyListTextViewVisibility
            emptyTextView.text = requireContext().getString(R.string.categories_empty_list)
            toolbar.toolbar.apply {
                title = it.title
                subtitle = it.subTitle
            }
        }
        categoryAdapter.setList(it.categoryList)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val title = requireContext().getString(R.string.app_name)
        val subTitle = requireContext().getString(R.string.category_subtitle)
        val interactor = CategoryInteractor(App.getApi())
        val factory = CategoryFactory(title, subTitle, interactor)
        categoryViewModel =
            ViewModelProviders.of(this, factory)
                .get(CategoryViewModel::class.java)

        observe(categoryViewModel.stateLiveData, stateObserver)

        categoryViewModel.onActivityCreated(savedInstanceState == null)

        categoryViewModel.categoryNavigationEvents.observe(viewLifecycleOwner, {
            when (it) {
                is CategoryNavigationEvents.GoToQuestionList -> goToQuestionList(
                    it.categoryID,
                    it.categoryName
                )
            }
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            categoryAdapter = CategoryAdapter(categoryViewModel)
            adapter = categoryAdapter
            val margin = requireContext().resources.getDimension(R.dimen.default_adapter_padding).toInt()
            addItemDecoration(AdapterItemDecorator(margin))
        }

        val sortOptions = requireContext().resources.getStringArray(R.array.category_sort_options)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sortOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.sortSpinner.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            categoryViewModel.onSwipeRefreshLayout()
        }

        binding.sortSpinner.onItemSelected {
            categoryViewModel.onSortSpinnerItemSelected(it)
        }
    }

    private fun goToQuestionList(categoryID: Int, categoryName: String) {
        navigate(
            R.id.action_categoryFragmentNew_to_questionListFragment2,
            QuestionsFragment.getBundle(categoryID, categoryName)
        )
    }
}