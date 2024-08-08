package com.albertson.spark_poc.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.albertson.spark_poc.R
import com.albertson.spark_poc.core.state.AccountState
import com.albertson.spark_poc.databinding.FragmentAccountBinding
import com.albertson.spark_poc.presentation.adapter.AccountListAdapter
import com.albertson.spark_poc.presentation.adapter.OnAccountClickListener
import com.albertson.spark_poc.presentation.viewmodel.CreateAccountBottomSheetViewModel
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import kotlinx.coroutines.launch


class AccountFragment : Fragment(), OnAccountClickListener {


    private lateinit var binding: FragmentAccountBinding
    private lateinit var registerUserViewModel: RegisteredUserViewModel
    private lateinit var createAccountBottomSheetViewModel: CreateAccountBottomSheetViewModel
    private lateinit var registeredUserViewModel: RegisteredUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        registerUserViewModel =
            ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]
        createAccountBottomSheetViewModel =
            ViewModelProvider(requireActivity())[CreateAccountBottomSheetViewModel::class.java]
        registeredUserViewModel =
            ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val user = registeredUserViewModel.fetchLoggedInUser(true)
                binding.userName.text = "Hello " + user.firstName
            }
        }

        binding.logout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    val user = registeredUserViewModel.fetchLoggedInUser(true)
                    println("45 resetting the user logged in value testing crash ${user.emailAddress}")
                    registerUserViewModel.updateUser(false, user.emailAddress)
                    findNavController().popBackStack()
                }
            }
        }

        setAdapter()

        return binding.root
    }

    private fun setAdapter() {
        val accountItemList = listOf(
            AccountState(title = "Purchases", subTitle = "View status, history and receipts.", icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_purchase)),
            AccountState(title = "Pharmacy & health", subTitle = "Manage prescriptions and more.", icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_pharmacy)),
            AccountState(title = "Profile & preferences", subTitle = "Manage your account and preferences.", icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_profile)),
            AccountState(title = "Community Buying", subTitle = "Manage community, member and products.", icon = ContextCompat.getDrawable(requireActivity(), R.drawable.community), enable = true),
            AccountState(title = "Family members", subTitle = "Manage shared access.", icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_family_member)),
            AccountState(title = "FreshPass", subTitle = "Start 30-day trial for free delivery!", icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_freshpass)),
            AccountState(title = "Schedule & Save", subTitle = "Manage your scheduled orders.", icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_schedule)),
            AccountState(title = "Wallet", subTitle = "Manage payment and member card.", icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_wallet))
        )

        binding.rcvAccounts.adapter = AccountListAdapter(accountItemList, this)
    }

    override fun onAddClick(accountState: AccountState) {
        findNavController().navigate(R.id.action_accountFragment_to_communityListFragment)
    }

}