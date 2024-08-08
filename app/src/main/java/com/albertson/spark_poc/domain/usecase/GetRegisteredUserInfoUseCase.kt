package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.core.utils.Results
import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.domain.repository.IMemberRepository
import com.albertson.spark_poc.domain.repository.IRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject

class GetRegisteredUserInfoUseCase @Inject constructor(private val iRegistration: IRegistration) {
    suspend operator fun invoke(email : String) : Flow<Results<Registration>> = flow{
        //emit(Results.Loading)
        try {
            emit(Results.Success(iRegistration.getRegisteredUserInfo(email)))
        }catch (e : Exception){
            emit(Results.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)


}