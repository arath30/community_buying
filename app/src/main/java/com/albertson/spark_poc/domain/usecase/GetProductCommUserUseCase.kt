package com.albertson.spark_poc.domain.usecases

import com.albertson.spark_poc.core.utils.Results
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.domain.repository.IProductCommUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetProductCommUserUseCase @Inject constructor(private val iProductUserCommunityRepository: IProductCommUserRepository) {

    operator fun invoke(communityId: Int): Flow<Results<List<ProductCommunityUser>>> = flow {
        emit(Results.Loading)
        try {
            emit(Results.Success(iProductUserCommunityRepository.getProductCommUserList(communityId)))
        }catch (e: Exception){
            emit(Results.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getProductCommUserListByUserId(communityName: String, userId: Int): Flow<Results<List<ProductCommunityUser>>> = flow {
        emit(Results.Loading)
        try {
            emit(Results.Success(iProductUserCommunityRepository.getProductCommUserListByUserId(communityName, userId)))
        }catch (e: Exception){
            emit(Results.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun deleteProduct(productId: Int){
        iProductUserCommunityRepository.deleteProductFromList(productId)
    }

    suspend fun updateProduct(productId: Int, quantity: Int) {
        iProductUserCommunityRepository.updateProduct(productId, quantity)
    }
}