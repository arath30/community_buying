package com.albertson.spark_poc.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.databinding.ProductListItemsBinding

class ProductListAdapter(private val productItem : List<ProductCommunityUser>, private val listener: OnProductClickListener) : RecyclerView.Adapter<ProductListViewHolder>(){

    private lateinit var binding : ProductListItemsBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        binding = ProductListItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val items = productItem[position]
        holder.bind(items, listener)
    }

    override fun getItemCount(): Int {
       return productItem.size
    }
}

class ProductListViewHolder(itemView : View) : ViewHolder(itemView) {

    fun bind(item : ProductCommunityUser, listener: OnProductClickListener){
        val title = itemView.findViewById<TextView>(R.id.productDescription)
        val add = itemView.findViewById<TextView>(R.id.addToCommunityLink)
        add.setOnClickListener{
            listener.onAddClick(item)
        }
        title.text = item.product_name
    }
}

interface OnProductClickListener{
    fun onAddClick(productItem: ProductCommunityUser)
}
