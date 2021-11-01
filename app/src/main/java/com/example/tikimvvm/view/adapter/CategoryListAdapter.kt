package com.example.tikimvvm.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tikimvvm.databinding.CategoryBinding
import com.example.tikimvvm.view.binding.BindableAdapter
import com.example.tikimvvm.models.Item

class CategoryListAdapter : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>(),
    BindableAdapter<Item> {

    private var categoryList: List<Item>? = listOf()

    override fun setData(items: List<Item>) {
        this.categoryList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList?.get(position)
        category.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = categoryList?.size ?: 0

    inner class ViewHolder(private var binding: CategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Item?) {
            binding.categoryName.text = category?.title
            Glide.with(itemView.context).load(category?.images?.first())
                .into(binding.categoryImage)
            //binding.executePendingBindings()
        }
    }
}