package com.albertson.spark_poc.domain.usecases

import com.albertson.spark_poc.core.utils.Results
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.domain.repository.IMemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMemberUseCase @Inject constructor(private val iMemberRepository: IMemberRepository) {

    operator fun invoke(communityId: Int): Flow<Results<List<Member>>> = flow {
        emit(Results.Loading)
        try {
            emit(Results.Success(iMemberRepository.getAllMemberList(communityId)))
        }catch (e: Exception){
            emit(Results.Error(errorMessage = e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getAllMemberListByEmailId(email: String, status: String) : Member {
        return iMemberRepository.getAllMemberListByEmailId(email, status)
    }
}