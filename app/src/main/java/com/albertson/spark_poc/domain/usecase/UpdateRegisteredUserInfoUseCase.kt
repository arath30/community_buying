package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.domain.repository.IRegistration
import javax.inject.Inject

class UpdateRegisteredUserInfoUseCase @Inject constructor(private val iRegistration: IRegistration) {

    suspend operator fun invoke(loggedIn: Boolean, email_address: String){
        iRegistration.updateRegisteredUser(loggedIn,email_address)
    }
}