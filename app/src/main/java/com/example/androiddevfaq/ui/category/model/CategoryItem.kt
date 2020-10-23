package com.example.androiddevfaq.ui.category.model

class CategoryItem {

    data class CategoryItemSrc(
        val id: Int?,
        val name: String?,
        val quantity: Int?,
        val priority: Int?,
        val logoPath: String?,
        val lastQuestionDate: String?
    )

    data class CategoryItemDst(
        val categoryID: Int?,
        val categoryName: String?,
        val size: Int?,
//        val titleSize: String?,
        val priority: Int?,
        val recyclerType: Int?,
        val logoPath: String?,
        val lastQuestionDate: String?
    )

    companion object {

        @JvmStatic
        fun CategoryItem.CategoryItemSrc.toCategoryItemDst(recyclerType: Int) =
            CategoryItemDst(
                categoryID = id,
                categoryName = name,
                size = quantity,
//                titleSize = "Вопросов в категории: ${quantity ?: 0}", //TODO("Refactor")
                recyclerType = recyclerType,
                priority = priority,
                logoPath = logoPath,
                lastQuestionDate = lastQuestionDate
            )

        @JvmStatic
        fun getRecyclerType(index: Int) = when(index % 2 == 0){
            true -> 0
            false -> 1
        }
    }
}