package com.albertson.spark_poc.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.databinding.BottomsheetAddProductBinding
import com.albertson.spark_poc.presentation.fragment.ProductListFragment.Companion.COMMUNITY_ID
import com.albertson.spark_poc.presentation.fragment.ProductListFragment.Companion.COMMUNITY_NAME
import com.albertson.spark_poc.presentation.viewmodel.CommunityViewModel
import com.albertson.spark_poc.presentation.viewmodel.MemberViewModel
import com.albertson.spark_poc.presentation.viewmodel.ProductCommUserViewModel
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class AddProductBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding: BottomsheetAddProductBinding
    private val qtyList = ArrayList<Int>()
    private var communityList = emptyList<Community>()
    private var communityNameList = mutableListOf<String>()
    private val TAG: String = this.javaClass.simpleName
    private lateinit var viewmodel: CommunityViewModel
    private lateinit var productCommUserViewModel: ProductCommUserViewModel
    private lateinit var registeredUserViewModel: RegisteredUserViewModel
    private lateinit var memberViewModel: MemberViewModel
    private lateinit var product: ProductCommunityUser
    private var selectedQty = 0
    private var selectedCommunityName = ""
    private var adminId = 0
    private var selectedCommunityId = 0
    private lateinit var user : Registration

    companion object {
        const val PRODUCT_NAME = "productName"
        const val PRODUCT_IMAGE = "productImage"
        const val PRODUCT_BASE_PRICE = "productBasePrice"
        const val PRODUCT_DISCOUNTED_PRICE = "productDiscountedPrice"
        const val PRODUCT_WEIGHT = "productWeight"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding = BottomsheetAddProductBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProvider(requireActivity())[CommunityViewModel::class.java]
        registeredUserViewModel =
            ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]
        productCommUserViewModel =
            ViewModelProvider(requireActivity())[ProductCommUserViewModel::class.java]
        memberViewModel =
            ViewModelProvider(requireActivity())[MemberViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProductData()

        binding.apply {
            tvTitle.text = product.product_name
            tvPrice.text = "$" + String.format(
                "%.2f", product.product_base_price.toDouble()
            ) + " / " + product.product_weight

            btnAddToCommunity.setOnClickListener {
                when (isCommunityAvailable) {
                    true -> addProductToCommunity()

                    else -> findNavController().navigate(R.id.action_homeFragment_to_createCommunityFragment)
                }
                dismiss()
            }

            ivClose.setOnClickListener {
                dismiss()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                  user = registeredUserViewModel.fetchLoggedInUser(true)
                if (user.userRole != "Admin") {
                    memberViewModel.getMemberInfo(user.emailAddress)
                    memberViewModel.memberInfo.collect{ it ->
                        it.success?.let { member ->
                            member.userId?.let {
                                val admin = registeredUserViewModel.fetchLoggedInUserById(it)
                                adminId = admin.usedId
                            }
                        }
                    }
                } else {
                    adminId = user.usedId
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                user = registeredUserViewModel.fetchLoggedInUser(true)

                memberViewModel.getMemberInfo(user.emailAddress)

                memberViewModel.memberInfo.collect { member ->
                    if (member.success != null) {
                        viewmodel.getCommunityList(userId = user.usedId)
                        member.success.communityId?.let { it1 ->
                            viewmodel.getCommunityByCommunityId(
                                communityId = it1
                            )
                        }
                    } else {
                        viewmodel.getCommunityList(userId = user.usedId)
                    }
                }
            }
        }

        binding.spQuantity.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedQty = qtyList[p2]
                val totalPrice = (product.product_base_price).toDouble() * selectedQty
                binding.tvTotalPrice.text = "$" + String.format("%.2f", totalPrice)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        binding.spCommunity.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (communityList.isNotEmpty()) {
                    selectedCommunityId = communityList[p2].communityId
                    selectedCommunityName = communityList[p2].communityName
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        setQuantityData()

        setCommunityData()
    }

    private fun setProductData() {
        arguments?.let {
            product = ProductCommunityUser(
                product_name = it.getString(PRODUCT_NAME).toString(),
                product_image = it.getString(PRODUCT_IMAGE).toString(),
                product_weight = it.getString(PRODUCT_WEIGHT).toString(),
                product_base_price = it.getString(PRODUCT_BASE_PRICE).toString(),
                product_discount_price = it.getString(PRODUCT_DISCOUNTED_PRICE).toString()
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setContentView(R.layout.bottomsheet_add_product)
            setOnShowListener {
                val bottomSheetView =
                    (it as? BottomSheetDialog)?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                        ?: return@setOnShowListener
                BottomSheetBehavior.from(bottomSheetView).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                }
            }
        }
    }

    private fun setCommunityData() {
        communityNameList.clear()
        communityList = emptyList()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.communityName.collect { it ->
                    it.success?.let {
                        communityList += it
                    }
                    communityList = communityList.filter { it.isActive }
                    setCommunityAdapter()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.communityData.collect { it ->
                    it.success?.let {
                        communityList += it
                    }
                    setCommunityAdapter()
                }
            }
        }
    }

    private fun setCommunityAdapter() {
        communityList = communityList.distinctBy { it.communityId } // remove duplicates

        communityList.filter { it.isActive && communityNameList.add(it.communityName)}

        communityNameList = communityNameList.toSet().toMutableList()

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, communityNameList
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spCommunity.adapter = adapter
        binding.isCommunityAvailable = communityNameList.size > 0
    }

    private fun setQuantityData() {
        qtyList.clear()
        var i = 1
        while (i <= 10) {
            qtyList.add(i)
            ++i
        }

        val adapter: ArrayAdapter<Int> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, qtyList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spQuantity.adapter = adapter
    }

    private fun addProductToCommunity() {
        product.product_quantity = selectedQty
        product.community_id = selectedCommunityId
        product.community_name = selectedCommunityName
        product.member_name = user.firstName + " " + user.lastName
        product.member_id = user.memberId
        product.userId = adminId

        productCommUserViewModel.insert(product)

        SuccessBottomSheetFragment().apply { arguments = Bundle().apply {
            putString(COMMUNITY_NAME, product.community_name)
            putInt(COMMUNITY_ID, product.community_id)
        } }.show(requireActivity().supportFragmentManager, TAG)
    }
}