package com.albertson.spark_poc.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.databinding.MemberListItemsBinding

class MemberListAdapter(private val memberList: List<Member>, private val onClickListener : OnClickListener ) :
    RecyclerView.Adapter<MemberItemViewHolder>() {

    private lateinit var binding: MemberListItemsBinding
    private var isAdmin: Boolean? = false
    private var userId: Int? = -1
    private var showDeleteCheckBox = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemViewHolder {
        binding = MemberListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MemberItemViewHolder, position: Int) {

        val memberItem = memberList[position]
        holder.binds(memberItem,userId,showDeleteCheckBox,onClickListener,isAdmin)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(position, memberItem.firstName+" "+memberItem.lastName)
        }
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    fun showCheckBox(showCheck: Boolean, userId: Int, isAdmin: Boolean?) {
        this.isAdmin = isAdmin
        this.userId = userId
        showDeleteCheckBox = showCheck
        notifyDataSetChanged()
    }
}

class MemberItemViewHolder(itemView: View) : ViewHolder(itemView) {
    fun binds(memberItem: Member,
              userId: Int?,
              showDeleteCheckBox: Boolean,
              onClickListener: OnClickListener,
              isAdmin: Boolean?) {
        val memberName = itemView.findViewById<TextView>(R.id.listItem)
        val memberStatus = itemView.findViewById<TextView>(R.id.memberStatus)
        var statusIndicator = itemView.findViewById<AppCompatImageView>(R.id.statusIndicator)
        val checkBox = itemView.findViewById<CheckBox>(R.id.cbDeleteMember)
        memberName.text = memberItem.firstName+" "+memberItem.lastName
        memberStatus.text = memberItem.status
        if (memberItem.status.equals("Active")) {
            statusIndicator.setImageResource(R.drawable.active_member)
        }


        if (showDeleteCheckBox){
            if(isAdmin == true){
                checkBox.visibility = View.VISIBLE
            }
            else if (userId != null){
                if (userId == memberItem.memberId){
                    checkBox.visibility = View.VISIBLE
                }
            }
        }else{
            checkBox.visibility = View.GONE
        }

        checkBox.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                onClickListener.onCheckClick(memberItem)
            }
        }
    }
}

interface OnClickListener{
    fun onClick(position : Int, item : String)
    fun onCheckClick(member: Member): Boolean
}
