package com.example.androiddevfaq.ui.favourites.adapter

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.androiddevfaq.R
import com.example.androiddevfaq.base.BaseAdapter
import com.example.androiddevfaq.databinding.ItemFavouritesFirstBinding
import com.example.androiddevfaq.databinding.ItemFavouritesSecondBinding
import com.example.androiddevfaq.utils.mapper.AdapterMapper

class FavouriteAdapter : BaseAdapter<FavouritesHolders.BaseFavouritesHolder>() {

    private var favourites = listOf<AdapterMapper.FavouriteItemRecycler>()

    fun setData(newList: List<AdapterMapper.FavouriteItemRecycler>) {
        favourites = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritesHolders.BaseFavouritesHolder {
        return when(viewType) {
            0 -> FavouritesHolders.FirstFavouriteHolder(parent.inflateBinding(ItemFavouritesFirstBinding::inflate))
            else -> FavouritesHolders.SecondFavouriteHolder(parent.inflateBinding(ItemFavouritesSecondBinding::inflate))
        }
    }

    override fun onBindViewHolder(holder: FavouritesHolders.BaseFavouritesHolder, position: Int) {
        holder.bind(favourites[position].title)
        setAnimation(holder.itemView)
    }

    override fun getItemCount() = favourites.size

    override fun getItemViewType(position: Int)
            = favourites[position].recyclerType

    private fun setAnimation(view: View) {
        val anim = AnimationUtils.loadAnimation(view.context, R.anim.recycler)
        view.startAnimation(anim)
    }
}