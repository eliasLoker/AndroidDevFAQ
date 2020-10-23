package com.example.androiddevfaq.ui.category.adapter

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.ItemCategoryFirstTypeBinding
import com.example.androiddevfaq.databinding.ItemCategorySecondTypeBinding
import com.example.androiddevfaq.base.BaseAdapter
import com.example.androiddevfaq.ui.category.model.CategoryItem

class CategoryAdapter(
    private val listener: CategoryAdapterListener
) : BaseAdapter<CategoryHolders.BaseCategoryHolder>() {

    private var categoryList = listOf<CategoryItem.CategoryItemDst>()

    fun setList(newList: List<CategoryItem.CategoryItemDst>) {
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
        val context = categoryHolder.itemView.context
        val resources = context.resources

        val name = categoryList[position].categoryName ?: resources.getString(R.string.category_empty_name)
        val quantity = "${categoryList[position].size ?: 0}"
        val titleSize = String.format(resources.getString(R.string.category_questions_quantity), quantity)
        val lastQuestionDate = categoryList[position].lastQuestionDate ?: resources.getString(R.string.category_empty_last_date)
        categoryHolder.bind(name, titleSize, lastQuestionDate)

        Glide.with(context)
            .load(Uri.parse("file:///android_asset/${categoryList[position].logoPath}"))
            .placeholder(R.drawable.shape_rectangle_categories_adapter)
            .error(R.drawable.shape_rectangle_categories_adapter)
            .into(categoryHolder.categoryLogo)

        categoryHolder.binding.root.setOnClickListener {
            listener.onCategoryClick(position)
        }

        setAnimation(categoryHolder.itemView)
    }

    override fun getItemCount() = categoryList.size

    override fun getItemViewType(position: Int) = categoryList[position].recyclerType ?: 0

    private fun setAnimation(view: View) {
        val anim = AnimationUtils.loadAnimation(view.context, R.anim.recycler)
        view.startAnimation(anim)
    }
}