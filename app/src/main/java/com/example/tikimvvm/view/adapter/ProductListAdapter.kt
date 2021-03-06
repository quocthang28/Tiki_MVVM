package com.example.tikimvvm.view.adapter

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tikimvvm.databinding.ProductBinding
import com.example.tikimvvm.models.DataX
import com.example.tikimvvm.utils.NumberUtils
import com.example.tikimvvm.view.binding.BindableAdapter
import java.util.*

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ViewHolder>(), BindableAdapter<DataX> {

    private var productList: List<DataX>? = listOf()

    override fun setData(items: List<DataX>) {
        productList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ProductBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList?.get(position)
        product.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = productList?.size ?: 0

    inner class ViewHolder(private var binding: ProductBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(product: DataX?) {
            binding.productName.text = product?.name
            binding.productPrice.text = NumberUtils.formatCurrency(product?.price)
            Glide.with(itemView.context).load(product?.thumbnail_url)
                    .into(binding.productImage)
            //binding.executePendingBindings()
        }

    }

}