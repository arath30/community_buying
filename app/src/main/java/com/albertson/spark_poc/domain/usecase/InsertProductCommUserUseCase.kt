package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.domain.repository.IProductCommUserRepository
import javax.inject.Inject

class InsertProductCommUserUseCase @Inject constructor(private val iProductUserCommunityRepository: IProductCommUserRepository) {
    suspend fun addProduct(productCommunityUser: ProductCommunityUser){
        iProductUserCommunityRepository.insertProduct(productCommunityUser)
    }
}