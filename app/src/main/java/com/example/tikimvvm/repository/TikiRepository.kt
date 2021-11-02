package com.example.tikimvvm.repository

import androidx.lifecycle.MutableLiveData
import com.example.tikimvvm.db.TikiDatabase
import com.example.tikimvvm.db.dao.CategoryDAO
import com.example.tikimvvm.db.entity.Category
import com.example.tikimvvm.models.ProductModel
//import com.example.tikimvvm.network.RestClient
import com.example.tikimvvm.network.RestClient
import com.example.tikimvvm.models.Item

class TikiRepository private constructor(private val dao: CategoryDAO) {
    private var categoryList: MutableList<Item> = mutableListOf()

    private var productList: MutableLiveData<List<ProductModel>> =
            MutableLiveData<List<ProductModel>>().apply { value = emptyList() }

    companion object {
        private var instance: TikiRepository? = null
        fun getInstance(dao: CategoryDAO): TikiRepository {
            if (instance == null) {
                synchronized(this) {
                    instance = TikiRepository(dao)
                }
            }
            return instance!!
        }
    }

    suspend fun getCategoryList(cursor: Int, limit: Int) =
            RestClient.getInstance().getApiService().getAllCategory(cursor, limit)


    suspend fun getProductList(cursor: Int, limit: Int) =
            RestClient.getInstance().getApiService().getAllProduct(cursor, limit)

    suspend fun getNextPageProductList(page: Int, limit: Int) = RestClient.getInstance().getApiService().getNextPageProductList(page, limit)

    //database related method
    suspend fun checkEmpty(): Category? {
        return dao.checkEmptyTable()
    }

    suspend fun getAllCategory(): MutableList<Category> {
        return dao.getAllCategories()
    }

    suspend fun saveCategories(categories: List<Item>) {
        for (category in categories) {
            dao.insertCategory(Category(0, category.images.first(), category.title!!))
        }
    }
}