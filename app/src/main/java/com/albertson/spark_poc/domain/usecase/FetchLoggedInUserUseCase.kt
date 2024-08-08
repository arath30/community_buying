package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.domain.repository.IRegistration
import javax.inject.Inject

class FetchLoggedInUserUseCase @Inject constructor(private val iRegistration: IRegistration) {
    suspend operator fun invoke(loggedIn : Boolean) : Registration {
       return iRegistration.fetchLoggedInUser(loggedIn)
    }

    suspend operator fun invoke(userId : Int) : Registration {
       return iRegistration.getRegisteredUserById(userId)
    }
}