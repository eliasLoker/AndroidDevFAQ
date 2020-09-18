package com.example.androiddevfaq.ui.category.adapter

import android.view.ViewGroup
import com.example.androiddevfaq.databinding.ItemCategorySecondTypeBinding
import com.example.androiddevfaq.ui.base.BaseAdapter
import com.example.androiddevfaq.utils.mapper.AdapterMapper

class CategoryAdapter() : BaseAdapter<CategoryHolders.CategoryBaseHolder>() {
//class CategoryAdapter() : RecyclerView.Adapter<CategoryHolders.CategoryBaseHolder>() {

    private var categoryList = ArrayList<AdapterMapper.CategoryRecycler>()

    fun setList(newList: List<AdapterMapper.CategoryRecycler>) {
        categoryList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryHolders.CategoryBaseHolder {
        return when (viewType) {
            0 -> {
                CategoryHolders.SecondTypeItem(parent.inflateBinding(ItemCategorySecondTypeBinding::inflate))
            }
            else -> {
                CategoryHolders.SecondTypeItem(parent.inflateBinding(ItemCategorySecondTypeBinding::inflate))
            }
        }
    }

    override fun onBindViewHolder(holder: CategoryHolders.CategoryBaseHolder, position: Int) {
        holder.bind(categoryList[position].categoryName, categoryList[position].categorySize)
    }

    override fun getItemCount() = categoryList.size
}