package com.albertson.spark_poc.presentation.fragment

import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.databinding.FragmentMemberBinding
import com.albertson.spark_poc.presentation.adapter.MemberListAdapter
import com.albertson.spark_poc.presentation.adapter.OnClickListener
import com.albertson.spark_poc.presentation.viewmodel.CommunityViewModel
import com.albertson.spark_poc.presentation.viewmodel.CreateAccountBottomSheetViewModel
import com.albertson.spark_poc.presentation.viewmodel.MemberViewModel
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import kotlinx.coroutines.launch


class MemberFragment : Fragment(), OnMemberDeleteListener {
    private lateinit var binding: FragmentMemberBinding
    private lateinit var memberViewModel: MemberViewModel
    private lateinit var createAccountBottomSheetViewModel: CreateAccountBottomSheetViewModel
    private lateinit var registeredUserViewModel: RegisteredUserViewModel
    private lateinit var communityViewModel: CommunityViewModel
    private var memberInfoList = emptyList<Member>()
    private var adminInfo: String? = ""
    private val TAG: String = this.javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMemberBinding.inflate(inflater, container, false)
        memberViewModel = ViewModelProvider(requireActivity())[MemberViewModel::class.java]

        createAccountBottomSheetViewModel =
            ViewModelProvider(requireActivity())[CreateAccountBottomSheetViewModel::class.java]
        registeredUserViewModel =
            ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]

        communityViewModel =
            ViewModelProvider(requireActivity())[CommunityViewModel::class.java]

        memberViewModel.communityId?.let {
            communityViewModel.getCommunityByCommunityId(communityId = it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                communityViewModel.communityData.collect { it ->
                    it.success?.let {
                        val user = registeredUserViewModel.fetchLoggedInUserById(it.userId)
                        if (user.userRole != "Admin") {
                            binding.addMember.visibility = View.VISIBLE
                        }
                        binding.adminName.text = user.firstName + " " + user.lastName
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val user = registeredUserViewModel.fetchLoggedInUser(true)
              //  binding.adminName.text = user.firstName + " " + user.lastName
                if (user.userRole != "Admin") {
                    binding.addMember.visibility = View.GONE
                   /* memberViewModel.getMemberInfo(user.emailAddress)
                    memberViewModel.userInfo.collect{ it ->
                        it.success?.let { member ->
                            member.userId?.let {
                               val admin = registeredUserViewModel.fetchLoggedInUserById(it)
                                binding.adminName.text = admin.firstName + " " + admin.lastName
                            }
                        }
                    }*/
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                createAccountBottomSheetViewModel.userName.collect {
                    adminInfo = it.toString()
                    registeredUserViewModel.fetchRegisteredUserInfo(adminInfo.toString())
                }
            }
        }

        memberViewModel.communityId?.let { memberViewModel.getMembersList(it) }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val user = registeredUserViewModel.fetchLoggedInUser(true)
                val isAdmin = user.userRole.equals("Admin")
                memberViewModel.memberList.collect {
                    memberInfoList = it.success!!
                    binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                    val memberAdapter = MemberListAdapter(memberInfoList, object : OnClickListener {
                        override fun onClick(position: Int, item: String) {
                            // findNavController().navigate(R.id.action_addMemberFragment_to_memberFragment)
                        }

                        override fun onCheckClick(member: Member): Boolean {
                            val bottomSheetFragment = DeleteMemberBottomSheetFragment()
                            bottomSheetFragment.setOnDeleteMemberListener(member,this@MemberFragment)
                            openDeleteConfirmationDialog(bottomSheetFragment)
                            return true
                        }

                    })
                    binding.recyclerView.adapter = memberAdapter

                    if (memberInfoList.isNotEmpty()) {
                        binding.recyclerView.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.visibility = View.GONE
                    }

                    memberViewModel.obj.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
                        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                            val isEdit = memberViewModel.obj.get()
                            memberAdapter.showCheckBox(isEdit, userId = user.memberId, isAdmin = isAdmin)

                        }

                    })
                }
            }
        }



        binding.addMember.setOnClickListener {
            findNavController().navigate(R.id.action_memberFragment_to_addMemberFragment)
        }

        return binding.root
    }

    private fun openDeleteConfirmationDialog(
        bottomSheetFragment: DeleteMemberBottomSheetFragment,
    ) {
        bottomSheetFragment.apply { arguments = Bundle().apply {
        } }.show(requireActivity().supportFragmentManager,TAG)
    }

    override fun onDelete(member: Member) {
        memberViewModel.deleteMember(member)
    }
}