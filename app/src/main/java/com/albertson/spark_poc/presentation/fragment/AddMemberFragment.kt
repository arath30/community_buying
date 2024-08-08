package com.albertson.spark_poc.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.databinding.FragmentAddMemberBinding
import com.albertson.spark_poc.presentation.viewmodel.MemberViewModel
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import kotlinx.coroutines.launch

class AddMemberFragment : Fragment() {

   private lateinit var binding : FragmentAddMemberBinding
   private lateinit var memberViewModel: MemberViewModel
   private lateinit var viewModel: RegisteredUserViewModel
   private lateinit var user: Registration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddMemberBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]
        memberViewModel = ViewModelProvider(requireActivity())[MemberViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                user = viewModel.fetchLoggedInUser(true)
            }
        }

        binding.addMember.setOnClickListener {
            val memberFirstName = binding.firstName.text.toString()
            val memberLastName = binding.memberLastName.text.toString()
            val memberEmail = binding.memberEmail.text.toString()

            if(memberFirstName.isEmpty() || memberLastName.isEmpty() || memberEmail.isEmpty()){
                Toast.makeText(context,"Please enter all fields properly", Toast.LENGTH_SHORT).show()
            }else{
                val member = Member(
                    firstName = memberFirstName,
                    lastName = memberLastName,
                    email = memberEmail,
                    communityName = memberViewModel.communityName,
                    communityId = memberViewModel.communityId,
                    userId = user.usedId,
                    isActive = false)
                memberViewModel.addMember(member)
                findNavController().popBackStack()
            }

        }
        return binding.root
    }


}