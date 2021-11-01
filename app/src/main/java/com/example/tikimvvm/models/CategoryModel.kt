package com.example.tikimvvm.models

import com.google.gson.annotations.SerializedName

data class CategoryList(
    @SerializedName("items")
    val items: List<CategoryModel>
)

data class CategoryModel(

    @SerializedName("category_id")
    val category_id: Int?,

    @SerializedName("images")
    val images: List<String>?,

    @SerializedName("title")
    val title: String?
)