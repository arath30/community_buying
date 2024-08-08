package com.albertson.spark_poc.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.albertson.spark_poc.R
import com.albertson.spark_poc.databinding.FragmentAccountBinding
import com.albertson.spark_poc.presentation.viewmodel.CreateAccountBottomSheetViewModel
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import kotlinx.coroutines.launch


class AccountFragment : Fragment() {


    private lateinit var binding : FragmentAccountBinding
    private lateinit var registerUserViewModel: RegisteredUserViewModel
    private lateinit var createAccountBottomSheetViewModel: CreateAccountBottomSheetViewModel
    private lateinit var registeredUserViewModel: RegisteredUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater,container,false)
        registerUserViewModel =
            ViewModelProvider(requireActivity()).get(RegisteredUserViewModel::class.java)
        createAccountBottomSheetViewModel =
            ViewModelProvider(requireActivity()).get(CreateAccountBottomSheetViewModel::class.java)
        registeredUserViewModel =
            ViewModelProvider(requireActivity()).get(RegisteredUserViewModel::class.java)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val user = registeredUserViewModel.fetchLoggedInUser(true)
                binding.userName.text = "Hello "+ user.firstName
            }
        }
        binding.communityCard.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_communityListFragment)
        }
        binding.logout.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                   val user = registeredUserViewModel.fetchLoggedInUser(true)
                    println("45 resetting the user logged in value testingcrash ${user.emailAddress}")
                    registerUserViewModel.updateUser(false,user.emailAddress)
                    findNavController().popBackStack()
                }
                }



        }
        return binding.root
    }


}