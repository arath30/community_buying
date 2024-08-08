package com.albertson.spark_poc.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LOGGER
import androidx.recyclerview.widget.LinearLayoutManager
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.databinding.FragmentCommunityListBinding
import com.albertson.spark_poc.presentation.adapter.CommunityListAdapter
import com.albertson.spark_poc.presentation.fragment.ProductListFragment.Companion.COMMUNITY_ID
import com.albertson.spark_poc.presentation.fragment.ProductListFragment.Companion.COMMUNITY_NAME
import com.albertson.spark_poc.presentation.viewmodel.CommunityViewModel
import com.albertson.spark_poc.presentation.viewmodel.MemberViewModel
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import kotlinx.coroutines.launch


class CommunityListFragment : Fragment(), OnAcceptInviteListener, OnCommunityDeleteListener {
    private var memberDetails: Member? = null
    private lateinit var binding: FragmentCommunityListBinding
    private lateinit var viewmodel: CommunityViewModel
    private var communityList = emptyList<Community>()
    private lateinit var registeredUserViewModel: RegisteredUserViewModel
    private lateinit var memberViewModel: MemberViewModel
    private lateinit var user: Registration
    private var adminName = ""
    private var adminEmail = ""
    private var communityName = ""
    private var communityUserId = 0
    private val TAG: String = this.javaClass.simpleName

    companion object {
        val newInstance = CommunityListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCommunityListBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProvider(requireActivity())[CommunityViewModel::class.java]
        registeredUserViewModel =
            ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]
        memberViewModel = ViewModelProvider(requireActivity())[MemberViewModel::class.java]


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                user = registeredUserViewModel.fetchLoggedInUser(true)

                if (::memberViewModel.isInitialized) {
                    memberViewModel.getMemberInfo(user.emailAddress)
                }
                if (user.userRole == "Admin") {
                    rejectedMember()
                }
                memberViewModel.memberInfo.collect { member ->
                    if (member.success != null) {
                        memberDetails = member.success

                        viewmodel.getCommunityList(userId = user.usedId)
                        member.success.communityId?.let { it1 ->
                            viewmodel.getCommunityByCommunityId(
                                communityId = it1
                            )
                        }

                        if (user.userRole == "Admin") {
                            binding.cardView2.visibility = View.GONE
                        } else {
                            if (member.success.status.equals("Pending")) {
                                val admin = member.success.userId?.let {
                                    registeredUserViewModel.fetchLoggedInUserById(
                                        it
                                    )
                                }
                                communityName = member.success.communityName
                                adminEmail = admin?.emailAddress ?: ""
                                adminName = admin?.firstName + " "+ admin?.lastName
                                binding.memberInvite.text = String.format(getString(R.string.community_invite), adminName,communityName)
                                binding.cardView2.visibility = View.VISIBLE
                            } else {
                                rejectedMember()
                            }
                        }
                    } else {
                        viewmodel.getCommunityList(userId = user.usedId)
                    }
                    setData()
                }
            }
        }

        binding.createCommunity.setOnClickListener {
            findNavController().navigate(R.id.action_communityListFragment_to_createCommunityFragment)
        }

        binding.viewCommunity.setOnClickListener {
            val viewInviteBottomSheet = ViewInviteBottomSheet()
            viewInviteBottomSheet.setOnAcceptInviteListener(this@CommunityListFragment)
            viewInviteBottomSheet.apply {
                arguments = Bundle().apply {
                    putString(ViewInviteBottomSheet.ADMIN_NAME, adminName)
                    putString(ViewInviteBottomSheet.COMMUNITY_NAME, communityName)
                }
            }.show(requireActivity().supportFragmentManager, TAG)
        }

        binding.ivEditCommunity.setOnClickListener {
            if (viewmodel.obj.get()){
                viewmodel.obj.set(false)
            }else{
                viewmodel.obj.set(true)
            }
        }

        return binding.root
    }

    private suspend fun rejectedMember() {
        val memberData = memberViewModel.getAllMemberListByEmailId(user.emailAddress, "Rejected")
        if (memberData != null) { // memberData will be null if status condition not matched, don't remove it
            communityName = memberData.communityName
            val memberName = memberData.firstName + " "+ memberData.lastName
            binding.memberInvite.text = String.format(getString(R.string.rejected_invite_msg), memberName)
            binding.viewCommunity.visibility = View.GONE
            binding.cardView2.visibility = View.VISIBLE
            binding.ivClose.visibility = View.VISIBLE

            binding.ivClose.setOnClickListener{
                binding.cardView2.visibility = View.GONE
                memberData.status = "Declined"
                memberData.rejectedToUser = user.emailAddress
                memberViewModel.acceptInvite(memberData.memberId, "Declined", user.emailAddress)
            }
        } else {
            binding.cardView2.visibility = View.GONE
        }
    }

    private fun setData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.communityName.collect { it ->
                    it.success?.let {
                        communityList += it
                        if (it.isNotEmpty()) {
                            val user = registeredUserViewModel.fetchLoggedInUserById(it.first().userId)
                            val list = it.filter { it1 -> it1.isActive }
                            if (user.userRole != "Admin" && list.isNotEmpty()) {
                                binding.ivEditCommunity.visibility = View.VISIBLE
                                communityUserId = it.first().userId
                            } else {
                                binding.ivEditCommunity.visibility = View.GONE
                            }
                        }
                    }
                    setCommunityData()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.communityData.collect { it ->
                    it.success?.let {
                        communityList += it
                    }
                    setCommunityData()
                }
            }
        }
    }

    private fun setCommunityData() {
        communityList = communityList.distinctBy { it.communityId } // remove duplicates
        communityList = communityList.filter { it.isActive }

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val itemAdapter = CommunityListAdapter(
            memberDetails,
            communityList,
            object : CommunityListAdapter.OnClickListener {
                override fun onClick(community: Community) {
                    findNavController().navigate(R.id.action_communityListFragment_to_myCommunityDashBoard,
                        Bundle().apply {
                            putString(
                                COMMUNITY_NAME, community.communityName
                            )
                            putInt(
                                COMMUNITY_ID, community.communityId
                            )
                        })
                }

                override fun onCheckClick(community: Community,position: Int) {
                    val bottomSheetFragment = DeleteCommunityBottomSheetFragment()
                    bottomSheetFragment.setOnDeleteMemberListener(position,community,this@CommunityListFragment)
                    openDeleteConfirmationDialog(bottomSheetFragment)
                }
            })
        binding.recyclerView.adapter = itemAdapter

        viewmodel.obj.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val isEdit = viewmodel.obj.get()
                itemAdapter.showCheckBox(isEdit, communityUserId, user.userRole)
            }

        })

        if (communityList.isEmpty()) {
            binding.noCommunityFound.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.ivEditCommunity.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.noCommunityFound.visibility = View.GONE
            if (user.userRole == "Admin") {
                binding.ivEditCommunity.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        communityList = emptyList() //for clearing list when coming from any screen.
    }

    override fun onAcceptInvite() {
        binding.cardView2.visibility = View.GONE
        memberDetails?.status = "Active"
        memberDetails?.rejectedToUser = adminEmail
        memberDetails?.memberId?.let { memberViewModel.acceptInvite(it, "Active", adminEmail) }
        setCommunityData()
    }

    override fun onRejectedInvite() {
        binding.cardView2.visibility = View.GONE
        memberDetails?.status = "Rejected"
        memberDetails?.rejectedToUser = adminEmail
        memberDetails?.memberId?.let { memberViewModel.acceptInvite(it, "Rejected", adminEmail) }
    }

    private fun openDeleteConfirmationDialog(
        bottomSheetFragment: DeleteCommunityBottomSheetFragment,
    ) {
        bottomSheetFragment.apply { arguments = Bundle().apply {
        } }.show(requireActivity().supportFragmentManager,TAG)
    }

    override fun onDelete(community: Community,position: Int) {
        viewmodel.makeInActiveCommunity(community)
        communityList -= community
    }
}