package com.example.tikimvvm.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tikimvvm.db.dao.CategoryDAO
import com.example.tikimvvm.viewmodel.ProductViewModel

class ViewModelFactory(private val dao: CategoryDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(dao) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }

}