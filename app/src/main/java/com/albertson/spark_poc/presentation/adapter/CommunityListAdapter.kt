package com.albertson.spark_poc.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.databinding.CommunityListItemsBinding


class CommunityListAdapter(private val member: Member?, private val communityList: List<Community>, private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<CommunityListAdapter.CommunityViewHolder>() {

    private lateinit var binding : CommunityListItemsBinding
    private var showDeleteCheckBox = false
    private var communityUserId = 0
    private var userRole = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        binding =  CommunityListItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CommunityViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
       return communityList.size
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
                val communityItems = communityList[position].communityName
                if ((communityList[position].communityId == member?.communityId) && !member.status.equals("Active")){
                    holder.itemView.visibility = View.GONE
                }
                holder.bind(communityItems,communityList[position],showDeleteCheckBox, communityUserId, userRole, onClickListener,position)
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(communityList[position])
                }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showCheckBox(showCheck: Boolean, ctyUserId: Int, role: String) {
        showDeleteCheckBox = showCheck
        communityUserId = ctyUserId
        userRole = role
        notifyDataSetChanged()
    }

    class CommunityViewHolder(itemView: View) :
        ViewHolder(itemView) {
            fun bind(
                items: String,
                community: Community,
                showDeleteCheckBox: Boolean,
                communityUserId: Int,
                userRole: String,
                onClickListener: OnClickListener,
                position: Int){
                val communityItem = itemView.findViewById<TextView>(R.id.listItem)
                val checkBox = itemView.findViewById<CheckBox>(R.id.cbDeleteCommunity)
                communityItem.text = items

                if ((userRole == "Admin" && showDeleteCheckBox) || (showDeleteCheckBox && (community.userId == communityUserId))) {
                    checkBox.visibility = View.VISIBLE
                } else {
                    checkBox.visibility = View.GONE
                }

                checkBox.setOnCheckedChangeListener{ _, isChecked ->
                    if (isChecked){
                        onClickListener.onCheckClick(community,position)
                    }
                }

            }
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(community: Community)
        fun onCheckClick(community: Community,position: Int)
    }

}