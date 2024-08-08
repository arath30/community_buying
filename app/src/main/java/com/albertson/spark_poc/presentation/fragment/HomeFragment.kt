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
import androidx.recyclerview.widget.LinearLayoutManager
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.databinding.FragmentHomeBinding
import com.albertson.spark_poc.presentation.adapter.OnProductClickListener
import com.albertson.spark_poc.presentation.adapter.ProductListAdapter
import com.albertson.spark_poc.presentation.fragment.AddProductBottomSheetFragment.Companion.PRODUCT_BASE_PRICE
import com.albertson.spark_poc.presentation.fragment.AddProductBottomSheetFragment.Companion.PRODUCT_DISCOUNTED_PRICE
import com.albertson.spark_poc.presentation.fragment.AddProductBottomSheetFragment.Companion.PRODUCT_IMAGE
import com.albertson.spark_poc.presentation.fragment.AddProductBottomSheetFragment.Companion.PRODUCT_NAME
import com.albertson.spark_poc.presentation.fragment.AddProductBottomSheetFragment.Companion.PRODUCT_WEIGHT
import com.albertson.spark_poc.presentation.viewmodel.RegisteredUserViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnProductClickListener {

    private lateinit var binding : FragmentHomeBinding
    private val productListOne = mutableListOf<ProductCommunityUser>()
    private val productListTwo = mutableListOf<ProductCommunityUser>()
    private lateinit var viewModel: RegisteredUserViewModel
    private var dummyUserList = mutableListOf<Registration>()
    private val TAG: String = this.javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(requireActivity())[RegisteredUserViewModel::class.java]

        val userList = listOf(
            Registration(
                firstName = "Adam",
                lastName = "Bell",
                emailAddress = "adam.bell@gmail.com",
                userRole = "Admin"
            ),
            Registration(
                firstName = "Alan",
                lastName = "Baker",
                emailAddress = "alan.baker@gmail.com",
                userRole = "Admin"
            ),
            Registration(
                firstName = "Boris",
                lastName = "Grant",
                emailAddress = "boris.grant@gmail.com",
                userRole = "Admin"
            ),
            Registration(
                firstName = "David",
                lastName = "Fisher",
                emailAddress = "david.fisher@gmail.com",
                userRole = "Admin"
            )
        )

        dummyUserList.addAll(userList)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val modal = CreateAccountBottomSheet()
                val userList =viewModel.getUserCount()
                if(userList == 0){
                    viewModel.insertUser(dummyUserList)
                    activity?.supportFragmentManager.let { fragmentManager ->
                        fragmentManager?.let {
                            modal.show(it, CreateAccountBottomSheet.TAG)
                        }
                    }

                }
                else
                {
                    val user = viewModel.fetchLoggedInUser(true)
                    if(user !=null){
                        activity?.supportFragmentManager.let { fragmentManager ->
                            fragmentManager?.let {
                                if(user.loggedIn == false){
                                    modal.show(it, CreateAccountBottomSheet.TAG)
                                }
                                else{
                                    if(modal.isAdded){
                                        modal.dismiss()
                                    }

                                }

                            }
                        }
                    }
                    else{
                        activity?.supportFragmentManager.let { fragmentManager ->
                            fragmentManager?.let {
                                modal.show(it, CreateAccountBottomSheet.TAG)
                            }
                        }
                    }


                }
            }
        }

        val productsOne = listOf(
            ProductCommunityUser(product_name = "Jennie-O 93% Lean Ground Turkey Fresh - 16 Oz", product_weight = "ea", product_base_price = "4.99", product_discount_price = "4.49", product_image = ""),
            ProductCommunityUser(product_name = "Jennie-O 93% Lean Ground Turkey Fresh - 16 Oz", product_weight = "ea", product_base_price = "3.99", product_discount_price = "3.49", product_image = "")
        )

        val productsTwo = listOf(
            ProductCommunityUser(product_name = "Lawry's Herb & Garlic With Lemon Marinade - 12 Oz", product_weight = "ea", product_base_price = "6.99", product_discount_price = "6.49", product_image = ""),
            ProductCommunityUser(product_name = "Lawry's Herb & Garlic With Lemon Marinade", product_weight = "ea", product_base_price = "2.99", product_discount_price = "2.49", product_image = "")
        )

        productListOne.addAll(productsOne)

        productListTwo.addAll(productsTwo)

        binding.productRecyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.productRecyclerViewTwo.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        val productListAdapter = ProductListAdapter(productListOne, this)
        val productListAdapterTwo = ProductListAdapter(productListTwo, this)
        binding.productRecyclerView.adapter = productListAdapter

        binding.productRecyclerViewTwo.adapter = productListAdapterTwo

        binding.productRecyclerViewTwo.adapter?.notifyDataSetChanged()

        binding.account.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_accountFragment)
        }

        binding.cart.setOnClickListener {
            //findNavController().navigate(R.id.action_homeFragment_to_productListFragment)
        }

        return binding.root
    }

    override fun onAddClick(productItem: ProductCommunityUser) {
        val bundle =  Bundle()
        bundle.apply {
            putString(PRODUCT_NAME, productItem.product_name)
            putString(PRODUCT_WEIGHT, productItem.product_weight)
            putString(PRODUCT_BASE_PRICE, productItem.product_base_price)
            putString(PRODUCT_DISCOUNTED_PRICE, productItem.product_discount_price)
            putString(PRODUCT_IMAGE, productItem.product_image)
        }
        AddProductBottomSheetFragment().apply { arguments = bundle }.show(requireActivity().supportFragmentManager, TAG)
    }
}