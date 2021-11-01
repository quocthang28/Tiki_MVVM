package com.example.tikimvvm.db.dao

import androidx.room.*
import com.example.tikimvvm.db.entity.Category

@Dao
interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category): Int

    @Delete
    suspend fun deleteCategory(category: Category): Int

    @Query(value = "SELECT * FROM category_table")
    fun getAllCategories(): MutableList<Category>

    @Query(value = "DELETE FROM category_table")
    suspend fun deleteAllCategories(): Int

    @Query("SELECT * FROM category_table LIMIT 1")
    suspend fun checkEmptyTable(): Category?
}