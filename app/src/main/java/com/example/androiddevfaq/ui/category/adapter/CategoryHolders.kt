package com.example.androiddevfaq.ui.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.ItemCategoryFirstTypeBinding
import com.example.androiddevfaq.databinding.ItemCategorySecondTypeBinding
import kotlinx.android.synthetic.main.item_category_first_type.view.*
import kotlinx.android.synthetic.main.item_category_first_type.view.item_category_name
import kotlinx.android.synthetic.main.item_category_first_type.view.item_category_size
import kotlinx.android.synthetic.main.item_category_second_type.view.*

class CategoryHolders {

    abstract class CategoryBaseHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        abstract val categoryNameTextView: TextView
        abstract val categotySizeTextView: TextView

        abstract fun bind(categoryName: String, categorySize: String)
    }

    class FirstTypeItem(binding: ItemCategoryFirstTypeBinding) : CategoryBaseHolder(binding) {

        override val categoryNameTextView = binding.itemCategoryName
        override val categotySizeTextView = binding.itemCategorySize

        override fun bind(categoryName: String, categorySize: String) {
            categoryNameTextView.text = categoryName
            categotySizeTextView.text = categorySize
        }
    }

    class SecondTypeItem(binding: ItemCategorySecondTypeBinding) : CategoryBaseHolder(binding) {

        override val categoryNameTextView = binding.itemCategoryName
        override val categotySizeTextView = binding.itemCategorySize

        override fun bind(categoryName: String, categorySize: String) {
            categoryNameTextView.text = categoryName
            categotySizeTextView.text = categorySize
        }
    }

//    companion object CategoryFactory {
//
//        @JvmStatic
//        fun getHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
//
//        }
//    }
}