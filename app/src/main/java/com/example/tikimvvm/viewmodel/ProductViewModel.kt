package com.example.tikimvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tikimvvm.db.dao.CategoryDAO
import com.example.tikimvvm.models.DataX
import com.example.tikimvvm.repository.TikiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(private val dao: CategoryDAO) : ViewModel() {
    var productList: MutableLiveData<List<DataX>> =
            MutableLiveData<List<DataX>>().apply { value = emptyList() }

    private var tikiRepository = TikiRepository.getInstance(dao)

    fun getProductList(cursor: Int, limit: Int) {
        viewModelScope.launch {
            val response = tikiRepository.getProductList(cursor, limit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productList.postValue(response.body()?.data?.data)
                }
            }
        }
    }

    fun getNextPageProductList(page: Int, limit: Int) {
        viewModelScope.launch {
            val response = tikiRepository.getNextPageProductList(page, limit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productList.value = productList.value?.plus(response.body()?.data?.data!!)
                }
            }
        }
    }
}