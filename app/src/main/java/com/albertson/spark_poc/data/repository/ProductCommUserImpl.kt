package com.albertson.spark_poc.data.repository

import com.albertson.spark_poc.data.local.dao.ProductCommUserDao
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.domain.repository.IProductCommUserRepository
import javax.inject.Inject

class ProductCommUserImpl @Inject constructor(private val productCommunityUserDao: ProductCommUserDao):
 IProductCommUserRepository{

    override fun getProductCommUserList(communityId: Int): List<ProductCommunityUser> {
        return productCommunityUserDao.getAllCommunityList(communityId)
    }

    override suspend fun getProductCommUserListByUserId(communityName: String, user_id: Int): List<ProductCommunityUser> {
        return productCommunityUserDao.getProductCommUserListByUserId(communityName, user_id)
    }

    override suspend fun deleteProductFromList(productId: Int) {
        return productCommunityUserDao.deleteProductFromList(productId)
    }

    override suspend fun updateProduct(productId: Int, quantity: Int) {
        productCommunityUserDao.updateProduct(productId, quantity)
    }

    override suspend fun insertProduct(productCommunityUser: ProductCommunityUser) {
        productCommunityUserDao.insert(productCommunityUser)
    }


}