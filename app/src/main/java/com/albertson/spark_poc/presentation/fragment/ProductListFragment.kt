package com.albertson.spark_poc.presentation.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.databinding.FragmentProductListBinding
import com.albertson.spark_poc.presentation.adapter.CartProductListAdapter
import com.albertson.spark_poc.presentation.adapter.OnClickEvent
import com.albertson.spark_poc.presentation.viewmodel.MemberViewModel
import com.albertson.spark_poc.presentation.viewmodel.ProductCommUserViewModel
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {

    private var productList: List<ProductCommunityUser> = emptyList()
    private lateinit var binding: FragmentProductListBinding
    private lateinit var productCommUserViewModel: ProductCommUserViewModel
    private lateinit var registeredUserViewModel: RegisteredUserViewModel
    private lateinit var memberViewModel: MemberViewModel
    private var estTotal: Double? = 0.0
    private var userId = 0
    private var memberId = 0

    companion object {
        const val COMMUNITY_NAME = "communityName"
        const val COMMUNITY_ID = "communityId"
        const val PRODUCT_ADDED = "isProductAdded"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        productCommUserViewModel =
            ViewModelProvider(requireActivity())[ProductCommUserViewModel::class.java]
        registeredUserViewModel =
            ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]
        memberViewModel = ViewModelProvider(requireActivity())[MemberViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setData()
                val user = registeredUserViewModel.fetchLoggedInUser(true)
                if (user.userRole != "Admin") {
                    binding.btnCheckout.isEnabled = false
                    binding.btnCheckout.background.setTint(ContextCompat.getColor(requireContext(), R.color.grey))

                    memberId = user.memberId
                    (arguments?.getInt(COMMUNITY_ID)
                        ?: productCommUserViewModel.communityId)?.let { it1 ->
                        productCommUserViewModel.getProductList(
                            communityId = it1
                        )
                    }
                } else {
                    userId = user.usedId
                    productCommUserViewModel.getProductCommUserListByUserId(
                        communityName = arguments?.getString(COMMUNITY_NAME)
                            ?: productCommUserViewModel.communityName, userId = userId
                    )
                }
            }
        }

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(requireContext(), "Successfully checkout!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun setData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val user = registeredUserViewModel.fetchLoggedInUser(true)
                val isAdmin = user.userRole == "Admin"
                productCommUserViewModel.productList.collect {
                    productList = it.success!!

                    setCartList(isAdmin)
                }
            }
        }
    }

    private fun setCartList(isAdmin: Boolean) {

        binding.isProductAvailable = productList.isNotEmpty()

        if (productList.isNotEmpty()) {
            val itemAdapter = CartProductListAdapter(isAdmin, memberId, productList, object : OnClickEvent {
                override fun onClick(product: ProductCommunityUser) {

                }

                override fun onRemove(position: Int) {
                    productCommUserViewModel.deleteItem(
                        productList[position].product_id,
                        memberId,
                        userId
                    )
                }

                override fun onQuantityAdd(product: ProductCommunityUser, quantity: Int) {
                    estTotal = estTotal?.plus(product.product_discount_price.toDouble())
                    setTotalAmt()
                    productCommUserViewModel.updateItem(product.product_id, quantity)
                }

                override fun onQuantityMinus(product: ProductCommunityUser, quantity: Int) {
                    estTotal = estTotal?.minus(product.product_discount_price.toDouble())
                    setTotalAmt()
                    productCommUserViewModel.updateItem(product.product_id, quantity)
                }
            })
            binding.rvCartList.adapter = itemAdapter

            estTotal = productList.sumOf {
                it.product_discount_price.toDouble().times(it.product_quantity)
            }
            setTotalAmt()
        }
    }

    private fun setTotalAmt() {
        binding.tvEstimatedTotal.text = "$" + String.format("%.2f", estTotal)
    }
}