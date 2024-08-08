package com.albertson.spark_poc.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.databinding.SignInBottomSheetBinding
import com.albertson.spark_poc.presentation.viewmodel.CreateAccountBottomSheetViewModel
import com.albertson.spark_poc.presentation.viewmodel.MemberViewModel
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class CreateAccountBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: SignInBottomSheetBinding
    private lateinit var createAccountBottomSheetViewModel: CreateAccountBottomSheetViewModel
    private lateinit var registerUserViewModel: RegisteredUserViewModel
    private lateinit var memberViewModel: MemberViewModel
    private var userList = mutableListOf<Registration>()
    private var userEmail: String = ""
    private var adminInfo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = SignInBottomSheetBinding.inflate(
            inflater, container, false
        )

        createAccountBottomSheetViewModel =
            ViewModelProvider(requireActivity())[CreateAccountBottomSheetViewModel::class.java]
        registerUserViewModel =
            ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]
        memberViewModel = ViewModelProvider(requireActivity())[MemberViewModel::class.java]



        // click on create account
        binding.createAccount.setOnClickListener {

            createAccountBottomSheetViewModel.createAccount(binding.emailAddress.text.toString())



            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    createAccountBottomSheetViewModel.userName.collect {
                        adminInfo = it.toString()
                        registerUserViewModel.fetchRegisteredUserInfo(adminInfo)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    memberViewModel.getMemberInfo(binding.emailAddress.text.toString())
                }
            }

            registerUserViewModel.registeredUserInfo.distinctUntilChanged().observe(viewLifecycleOwner) {registeredUserState->
                binding.errorMsg.visibility = View.GONE
                userEmail = registeredUserState.success?.emailAddress ?: ""
                println("1 registered user email testingcrash $userEmail")
                println("2 email entered by user testingcrash ${binding.emailAddress.text.toString()}")
                if (userEmail != binding.emailAddress.text.toString()) {
                    println("3 registered user email and enter email is not the same testingcrash")
                    viewLifecycleOwner.lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            memberViewModel.memberInfo.collect { memberInfoState ->
                                println("4 we are fetching the member info here testingcrash ${memberInfoState.success}")
                                memberInfoState.success?.let { member ->
                                    println("5 we found the memberinfor state is not null and we have something to show here for member object testingcrash $member")
                                    if (userEmail != member.email) {
                                        println("6 registered user email and the member object email which we fetched are not the same testingcrash ${"userEmail "+userEmail} ${"member Email "+member.email} ")
                                        if (binding.emailAddress.text.toString() == member.email) {
                                            println("7 entered email addressed by user and the member object which we fetched are the same testingcrash ${"edit text data "+ binding.emailAddress.text.toString()} ${"member email "+ member.email}")
                                            userList.add(
                                                Registration(
                                                    firstName = memberInfoState.success.firstName,
                                                    lastName = memberInfoState.success.lastName,
                                                    emailAddress = memberInfoState.success.email,
                                                    userRole = memberInfoState.success.userRole,
                                                    memberId = memberInfoState.success.memberId,
                                                    loggedIn = true
                                                )
                                            )
                                            println("8 we have the user list which we are going to insert into the database testingcrash ${userList}")
                                            registerUserViewModel.insertUser(userList)
                                            println("9 data inserted ${userList}")
                                           /* registerUserViewModel.updateUser(
                                                loggedIn = true,
                                                binding.emailAddress.text.toString()
                                            )*/
                                            println("10 user updated")
                                        } else {
                                            println("11 entered email addressed by user and the member object email which we fetched are not the same testingcrash ${"edit text data "+ binding.emailAddress.text.toString()} ${"member email "+ member.email}")
                                            binding.errorMsg.visibility = View.VISIBLE  // need to fix
                                        }
                                    }
                                    println("12 registered user email and the member object email which we fetched are the same testingcrash ${"userEmail "+userEmail} ${"member Email "+member.email} ")
                                }
                                println("13 we found the memberinfor state is  null and we can not do anything about it but we can update the user which we entered by email testingcrash")
                                registerUserViewModel.updateUser(
                                    loggedIn = true,
                                    binding.emailAddress.text.toString()
                                )
                            }
                        }
                    }
                }
                println("14 registered user email and enter email are the same so updating the user testingcrash")
                registerUserViewModel.updateUser(
                    loggedIn = true,
                    binding.emailAddress.text.toString()
                )
            }

            dialog?.dismiss()
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val TAG = "CreateAccountBottomSheetDialog"
    }

    override fun onPause() {
        super.onPause()
        requireActivity().viewModelStore.clear()
    }
}