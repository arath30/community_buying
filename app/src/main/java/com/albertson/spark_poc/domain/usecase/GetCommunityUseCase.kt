package com.albertson.spark_poc.domain.usecases

import com.albertson.spark_poc.core.utils.Results
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.domain.repository.ICommunityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCommunityUseCase @Inject constructor(private val iCommunityRepository: ICommunityRepository) {

    operator fun invoke(userId: Int): Flow<Results<List<Community>>> = flow {
        emit(Results.Loading)
        try {
            emit(Results.Success(iCommunityRepository.getCommunityList(userId)))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun invokeCommunity(communityId: Int): Flow<Results<Community>> = flow {
        emit(Results.Loading)
        try {
            emit(Results.Success(iCommunityRepository.getAllCommunityByCommunityId(communityId)))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun invokeCommunityList(communityAdmin: String): Flow<Results<List<Community>>> = flow {
        emit(Results.Loading)
        try {
            emit(Results.Success(iCommunityRepository.getAllCommunityByCommunityAdmin(communityAdmin)))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)


}