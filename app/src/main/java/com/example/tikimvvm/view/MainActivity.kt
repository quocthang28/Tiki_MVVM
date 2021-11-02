package com.example.tikimvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tikimvvm.R
import com.example.tikimvvm.databinding.ActivityMainBinding
import com.example.tikimvvm.db.TikiDatabase
import com.example.tikimvvm.utils.ViewModelFactory
import com.example.tikimvvm.view.adapter.CategoryListAdapter
import com.example.tikimvvm.view.adapter.ProductListAdapter
import com.example.tikimvvm.viewmodel.CategoryViewModel
import com.example.tikimvvm.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var categoryListAdapter: CategoryListAdapter
    private lateinit var categoryViewModel: CategoryViewModel

    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var productViewModel: ProductViewModel

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        val categoryDAO = TikiDatabase.getInstance(application).categoryDAO
        val viewModelFactory = ViewModelFactory(categoryDAO)
        categoryViewModel = CategoryViewModel(categoryDAO)
        //productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel = ViewModelProvider(this, viewModelFactory)[ProductViewModel::class.java]

        activityMainBinding.categoryViewModel = categoryViewModel
        activityMainBinding.productViewModel = productViewModel

        activityMainBinding.lifecycleOwner = this

        getData()
        initializeRecyclerViews()
        initializeObservers()
    }

    private fun getData() {
        categoryViewModel.getCategoryList(0, 20)
        productViewModel.getProductList(0, 20)
    }

    private fun initializeRecyclerViews() {
        categoryListAdapter = CategoryListAdapter()
        activityMainBinding.categoryList.apply {
            layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryListAdapter
        }

        var nextPage = 1;
        productListAdapter = ProductListAdapter()
        activityMainBinding.productList.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = productListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        nextPage += 1
                        productViewModel.getNextPageProductList(nextPage, 10)
                        Log.i("myTag", "$nextPage")
                    }
                }
            })
        }
    }

    private fun initializeObservers() {
//        categoryViewModel.getCategoryList(0, 20).observe(this,
//                Observer {
//                    categoryListAdapter.setData(it)
//                })

//        productViewModel.getProductList(0, 20).observe(this, Observer {
//            productListAdapter.setData(it)
//        })
    }
}