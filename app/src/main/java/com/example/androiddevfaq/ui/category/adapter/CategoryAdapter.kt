package com.example.androiddevfaq.ui.category.adapter

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.ItemCategoryFirstTypeBinding
import com.example.androiddevfaq.databinding.ItemCategorySecondTypeBinding
import com.example.androiddevfaq.ui.base.BaseAdapter
import com.example.androiddevfaq.utils.mapper.AdapterMapper

class CategoryAdapter(
    private val listener: CategoryAdapterListener
) : BaseAdapter<CategoryHolders.BaseCategoryHolder>() {
//class CategoryAdapter() : RecyclerView.Adapter<CategoryHolders.CategoryBaseHolder>() {

    private var categoryList = listOf<AdapterMapper.CategoryItemRecycler>()

    fun setList(newList: List<AdapterMapper.CategoryItemRecycler>) {
        categoryList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryHolders.BaseCategoryHolder {
        return when (viewType) {
            0 -> {
                CategoryHolders.FirstTypeItem(parent.inflateBinding(ItemCategoryFirstTypeBinding::inflate))
            }
            else -> {
                CategoryHolders.SecondTypeItem(parent.inflateBinding(ItemCategorySecondTypeBinding::inflate))
            }
        }
    }

    override fun onBindViewHolder(
        categoryHolder: CategoryHolders.BaseCategoryHolder,
        position: Int
    ) {
        categoryHolder.bind(categoryList[position].categoryName, categoryList[position].titleSize)
        Log.d("AdapterDebug", "Pic: ${categoryList[position].logoPath}")
        Glide.with(categoryHolder.itemView.context)
            .load(Uri.parse("file:///android_asset/${categoryList[position].logoPath}"))
            .placeholder(R.drawable.shape_rectangle_categories_adapter)
            .error(R.drawable.shape_rectangle_categories_adapter)
            .into(categoryHolder.categoryLogo)
        categoryHolder.binding.root.setOnClickListener {
            listener.onCategoryClick(position)
        }
    }

    override fun getItemCount() = categoryList.size
}