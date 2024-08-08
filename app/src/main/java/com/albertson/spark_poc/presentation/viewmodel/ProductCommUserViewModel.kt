package com.albertson.spark_poc.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertson.spark_poc.core.state.ProductCommUserState
import com.albertson.spark_poc.core.utils.Results
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.domain.usecase.InsertProductCommUserUseCase
import com.albertson.spark_poc.domain.usecases.GetProductCommUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductCommUserViewModel @Inject constructor(private val insertUserUseCase: InsertProductCommUserUseCase,
    private val getProductCommUserUseCase: GetProductCommUserUseCase): ViewModel() {
    private val _productList = MutableStateFlow(ProductCommUserState())
    var productList: StateFlow<ProductCommUserState> = _productList.asStateFlow()
    var communityName = ""
    var communityId : Int? = 0

    fun getProductList(communityId: Int) = viewModelScope.launch {
        getProductCommUserUseCase.invoke(communityId).onEach { results ->
            when (results) {
                is Results.Error -> {
                    Log.d("CommunityViewModel", results.errorMessage)
                }

                is Results.Success -> {
                    _productList.value = ProductCommUserState(results.data)
                }

                Results.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    fun getProductCommUserListByUserId(communityName: String, userId: Int) = viewModelScope.launch {
            getProductCommUserUseCase.getProductCommUserListByUserId(communityName, userId).onEach { results ->
                when (results) {
                    is Results.Error -> {
                        Log.d("CommunityViewModel", results.errorMessage)
                    }

                    is Results.Success -> {
                        _productList.value = ProductCommUserState(results.data)
                    }

                    Results.Loading -> {

                    }
                }
            }.launchIn(viewModelScope)
    }

    fun deleteItem(productId: Int, memberId: Int, userId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            getProductCommUserUseCase.deleteProduct(productId)
            //need to update
            communityId?.let { getProductList(it) }
            getProductCommUserListByUserId(communityName, userId)
        }
    }

    fun updateItem(productId: Int, quantity: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            // No need to update
            getProductCommUserUseCase.updateProduct(productId, quantity)
        }
    }

    fun insert(productCommunityUser: ProductCommunityUser) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            insertUserUseCase.addProduct(productCommunityUser)
            //need to update
            getProductList(productCommunityUser.community_id)
            getProductCommUserListByUserId(productCommunityUser.community_name, productCommunityUser.userId)
        }
    }
}