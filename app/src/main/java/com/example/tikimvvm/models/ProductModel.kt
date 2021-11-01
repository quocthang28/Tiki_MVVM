package com.example.tikimvvm.models

import com.google.gson.annotations.SerializedName

data class ProductModel(

    @SerializedName("name")
    val name: String?,

    @SerializedName("price")
    val price: Int?,

    @SerializedName("quantity_sold")
    val quantity_sold: QuantitySoldX?,

    @SerializedName("rating_average")
    val rating_average: Int?,

    @SerializedName("thumbnail_url")
    val thumbnail_url: String?
)

data class QuantitySoldX(
    @SerializedName("text")
    val text: String?,

    @SerializedName("value")
    val value: Int?
)