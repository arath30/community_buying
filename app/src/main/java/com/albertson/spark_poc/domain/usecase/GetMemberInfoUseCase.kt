package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.core.utils.Results
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.domain.repository.IMemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMemberInfoUseCase @Inject constructor(private val iMemberRepository: IMemberRepository) {
    suspend operator fun invoke(email: String): Flow<Results<Member>> = flow {
        //emit(Results.Loading)
        try {
            emit(Results.Success(iMemberRepository.getRegisteredUserInfo(email)))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}