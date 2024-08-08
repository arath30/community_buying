package com.albertson.spark_poc.domain.repository

import com.albertson.spark_poc.data.local.entity.ProductCommunityUser

interface IProductCommUserRepository {
    fun getProductCommUserList(communityId: Int) : List<ProductCommunityUser>
    suspend fun getProductCommUserListByUserId(communityName: String, userId: Int) : List<ProductCommunityUser>
    suspend fun deleteProductFromList(productId: Int)
    suspend fun updateProduct(productId: Int, quantity: Int)
    suspend fun insertProduct(productCommunityUser: ProductCommunityUser)
}