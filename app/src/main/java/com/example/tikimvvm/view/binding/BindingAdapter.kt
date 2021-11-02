package com.example.tikimvvm.view.binding

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

interface BindableAdapter<T> {
    fun setData(items: List<T>)
}

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("dataList")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, items: List<T>) {
    if (recyclerView.adapter is BindableAdapter<*>) {
        (recyclerView.adapter as BindableAdapter<T>).setData(items)
    }
}
