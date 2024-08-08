package com.albertson.spark_poc.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.albertson.spark_poc.core.state.AccountState
import com.albertson.spark_poc.databinding.AdapterAccountListBinding

class AccountListAdapter(
    private val accountItem: List<AccountState>, private val listener: OnAccountClickListener
) : RecyclerView.Adapter<AccountListViewHolder>() {

    private lateinit var binding: AdapterAccountListBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListViewHolder {
        binding =
            AdapterAccountListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: AccountListViewHolder, position: Int) {
        binding.accountItem = accountItem[position]
        holder.itemView.setOnClickListener {
            listener.onAddClick(accountItem[position])
        }
        binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return accountItem.size
    }
}

class AccountListViewHolder(itemView: View) : ViewHolder(itemView)

interface OnAccountClickListener {
    fun onAddClick(accountState: AccountState)
}
