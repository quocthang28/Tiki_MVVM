package com.example.tikimvvm.view.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.tikimvvm.db.dao.CategoryDAO
import com.example.tikimvvm.db.entity.Category
import com.example.tikimvvm.models.Item
import com.example.tikimvvm.repository.TikiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel(private val dao: CategoryDAO) : BaseObservable() {

    @get:Bindable
    var categoryList: MutableList<Item> = mutableListOf()
//        private set(value) {
//            field = value
//        }

    private var tikiRepository = TikiRepository.getInstance(dao)

    fun getCategoryList(cursor: Int, limit: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val category = dao.checkEmptyTable()
            if (category != null) {
                val categories = dao.getAllCategories()
                for (cate in categories) {
                    categoryList.add(Item(cate.category_id, listOf(cate.images), cate.title))
                }
                notifyPropertyChanged(BR.categoryList)
            } else {
                val response = tikiRepository.getCategoryList(cursor, limit)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.data?.meta_data?.items?.let { categoryList.addAll(it) }
                        saveCategoriesToLocal(response.body()?.data?.meta_data?.items!!)
                        notifyPropertyChanged(BR.categoryList)
                    }
                }

            }
        }
    }

    private suspend fun saveCategoriesToLocal(categories: List<Item>) {
        Log.i("myTag", "Saving data")
        for (category in categories) {
            val cate: Category =
                Category(category.category_id!!, category.images.first(), category.title!!)
            dao.insertCategory(cate)
        }
    }
}