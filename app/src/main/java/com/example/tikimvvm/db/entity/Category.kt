package com.example.tikimvvm.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    val category_id: Int,

    @ColumnInfo(name = "category_image")
    val images: String,

    @ColumnInfo(name = "category_title")
    val title: String
)