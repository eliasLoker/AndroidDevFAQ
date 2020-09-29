package com.example.androiddevfaq.ui.category.event

import com.example.androiddevfaq.utils.mapper.AdapterMapper

class SetCategoryAdapterEvent(
    val categoryItemList: List<AdapterMapper.CategoryItemRecycler>
)