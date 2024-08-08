package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.domain.repository.IRegistration
import javax.inject.Inject

class GetRegistrationUserCountUseCase @Inject constructor(private val iRegistration: IRegistration) {

    suspend operator fun invoke() : Int{
        return iRegistration.getRegistrationCount()
    }
}