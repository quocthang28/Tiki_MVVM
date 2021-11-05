package com.example.tikimvvm.view

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tikimvvm.R
import com.example.tikimvvm.databinding.ActivityMainBinding
import com.example.tikimvvm.db.TikiDatabase
import com.example.tikimvvm.service.Actions
import com.example.tikimvvm.service.LocationService
import com.example.tikimvvm.utils.ViewModelFactory
import com.example.tikimvvm.view.adapter.CategoryListAdapter
import com.example.tikimvvm.view.adapter.ProductListAdapter
import com.example.tikimvvm.viewmodel.CategoryViewModel
import com.example.tikimvvm.viewmodel.ProductViewModel
import java.util.jar.Manifest

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

        findViewById<Button>(R.id.startService).let {
            it.setOnClickListener {
                if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
                } else {
                    actionOnService(Actions.START)
                }
            }
        }

        findViewById<Button>(R.id.stopService).let {
            it.setOnClickListener {
                actionOnService(Actions.STOP)
            }
        }

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
                        Log.i("myTag", "loading page $nextPage")
                        productViewModel.getNextPageProductList(nextPage, 10)
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

    private fun actionOnService(action: Actions) {
        Intent(this, LocationService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                print("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            print("Starting the service in < 26 Mode")
            startService(it)
        }
    }
}