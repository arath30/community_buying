package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.domain.repository.IRegistration
import javax.inject.Inject

class RegisteredUserUseCase @Inject constructor(private val iRegistration: IRegistration){
    suspend operator fun invoke(registration: List<Registration>){
         iRegistration.insertRegisteredUserInfo(registration)
    }
}