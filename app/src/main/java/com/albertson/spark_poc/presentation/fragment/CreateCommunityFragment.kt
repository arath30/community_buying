package com.albertson.spark_poc.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.databinding.FragmentCreateCommunityBinding
import com.albertson.spark_poc.presentation.viewmodel.CommunityViewModel
import com.albertson.spark_poc.presentation.viewmodel.CreateAccountBottomSheetViewModel
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateCommunityFragment : Fragment() {

    lateinit var binding: FragmentCreateCommunityBinding
    private lateinit var viewmodel: CommunityViewModel
    private lateinit var createAccountBottomSheetViewModel: CreateAccountBottomSheetViewModel
    private lateinit var registeredUserViewModel: RegisteredUserViewModel
    private lateinit var community: Community



    companion object {
        val newInstance = CreateCommunityFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreateCommunityBinding.inflate(inflater, container, false)

        viewmodel = ViewModelProvider(requireActivity())[CommunityViewModel::class.java]

        createAccountBottomSheetViewModel =
            ViewModelProvider(requireActivity())[CreateAccountBottomSheetViewModel::class.java]
        registeredUserViewModel =
            ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]


        binding.createCommunity.setOnClickListener {
            val communityItem = binding.communityName.text.toString()
            val list = viewmodel.communityName.value.success
            val isExist = isCommunityNameExist(list, communityItem)

            if (communityItem.isEmpty()) {
                Toast.makeText(
                    context, getString(R.string.please_enter_community_name), Toast.LENGTH_SHORT
                ).show()
            } else {
                if (list != null) {
                    if (isExist) {
                        binding.communityName.error =
                            getString(R.string.community_name_already_exist)
                    } else {


                        viewLifecycleOwner.lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                val user = registeredUserViewModel.fetchLoggedInUser(true)
                                community = Community(
                                    communityName = communityItem,
                                    userId = user.usedId,
                                    communityAdmin = user.firstName,
                                    status = "Pending",
                                    isActive = true
                                )
                                viewmodel.insert(community)
                            }
                        }
                        lifecycleScope.launch {
                            viewmodel.communityName.collectLatest {

                            }
                        }
                        requireActivity().onBackPressed()
                    }
                }
            }

        }
        return binding.root

    }

    private fun isCommunityNameExist(success: List<Community>?, name: String): Boolean {
        return success?.any {
            it.communityName.uppercase().replace(" ", "") == name.uppercase().replace(" ", "")
        } ?: false

    }

}