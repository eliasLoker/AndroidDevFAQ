package com.example.androiddevfaq.ui.category.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.androiddevfaq.databinding.ItemCategoryFirstTypeBinding
import com.example.androiddevfaq.databinding.ItemCategorySecondTypeBinding

class CategoryHolders {

    abstract class BaseCategoryHolder(val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        abstract val categoryNameTextView: TextView
        abstract val categorySizeTextView: TextView
        abstract val categoryLogo: ImageView
        abstract val categoryLastQuestionDate: TextView

        fun bind(categoryName: String, categorySize: String, categoryLastQuestion: String) {
            categoryNameTextView.text = categoryName
            categorySizeTextView.text = categorySize
            categoryLastQuestionDate.text = categoryLastQuestion
        }
    }

    class FirstTypeItem(binding: ItemCategoryFirstTypeBinding) : BaseCategoryHolder(binding) {

        override val categoryNameTextView = binding.itemCategoryName
        override val categorySizeTextView = binding.itemCategorySize
        override val categoryLogo = binding.itemCategoryImageView
        override val categoryLastQuestionDate = binding.lastQuestionDate
    }

    class SecondTypeItem(binding: ItemCategorySecondTypeBinding) : BaseCategoryHolder(binding) {

        override val categoryNameTextView = binding.itemCategoryName
        override val categorySizeTextView = binding.itemCategorySize
        override val categoryLogo = binding.itemCategoryImageView
        override val categoryLastQuestionDate = binding.lastQuestionDate
    }
}