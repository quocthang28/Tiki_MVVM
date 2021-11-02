package com.example.tikimvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    var nextPageUrl: MutableLiveData<String> = MutableLiveData<String>().apply { value = null }

    private var tikiRepository = TikiRepository.getInstance(dao)

    fun getProductList(cursor: Int, limit: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = tikiRepository.getProductList(cursor, limit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productList.postValue(response.body()?.data?.data)
                    nextPageUrl.postValue(response.body()?.data?.next_page)
                }
            }
        }
    }

    fun getNextPageProductList(page: Int, limit: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = tikiRepository.getNextPageProductList(page, limit)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productList.value = productList.value?.plus(response.body()?.data?.data!!)
                    nextPageUrl.postValue(response.body()?.data?.next_page)
                }
            }
        }
    }
}