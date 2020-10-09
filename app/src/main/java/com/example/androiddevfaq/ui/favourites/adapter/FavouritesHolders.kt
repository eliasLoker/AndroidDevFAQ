package com.example.androiddevfaq.ui.favourites.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.androiddevfaq.databinding.ItemFavouritesFirstBinding
import com.example.androiddevfaq.databinding.ItemFavouritesSecondBinding

class FavouritesHolders {

    abstract class BaseFavouritesHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        abstract val title: TextView

        fun bind(title: String) {
            this.title.text = title
        }
    }

    class FirstFavouriteHolder(binding: ItemFavouritesFirstBinding) : BaseFavouritesHolder(binding) {

        override val title: TextView = binding.title
    }

    class SecondFavouriteHolder(binding: ItemFavouritesSecondBinding) : BaseFavouritesHolder(binding) {

        override val title: TextView = binding.title
    }
}