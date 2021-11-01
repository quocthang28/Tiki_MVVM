package com.example.tikimvvm.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tikimvvm.databinding.ProductBinding
import com.example.tikimvvm.models.DataX
import com.example.tikimvvm.view.binding.BindableAdapter

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ViewHolder>(), BindableAdapter<DataX> {

    private var productList: List<DataX>? = listOf()

//    fun setData(categoryList: List<DataX>) {
//        this.productList = categoryList
//        notifyDataSetChanged()
//    }


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
            binding.productPrice.text = product?.price.toString()
            Glide.with(itemView.context).load(product?.thumbnail_url)
                    .into(binding.productImage)
            //binding.executePendingBindings()
        }
    }

    override fun setData(items: List<DataX>) {
        productList = items
        notifyDataSetChanged()
    }
}