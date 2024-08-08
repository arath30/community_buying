package com.albertson.spark_poc.data.repository

import com.albertson.spark_poc.data.local.dao.RegistrationDao
import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.domain.repository.IRegistration
import javax.inject.Inject

class RegistrationImpl @Inject constructor(private val registrationDao: RegistrationDao) : IRegistration {
    override suspend fun insertRegisteredUserInfo(registration: List<Registration>) {
        println("26 inserting the user info into registration testingcrash")
       val inserted = registrationDao.insertRegisteredUser(registration)
        println("value inserted for the user testingcrash ${inserted}")
    }

    override suspend fun getRegisteredUserInfo(email: String): Registration {
        return registrationDao.getRegisteredUser(email)
    }

    override suspend fun getRegisteredUserById(userId: Int): Registration {
        return registrationDao.getRegisteredUserById(userId)
    }

    override suspend fun updateRegisteredUser(loggedIn: Boolean, email_address: String) {
        println("25 updating the user info testingcrash $loggedIn $email_address")
        val value = registrationDao.updateRegisteredUserInfo(loggedIn,email_address)
        println("value updated for the user testingcrash $email_address $value")
    }

    override suspend fun fetchLoggedInUser(loggedIn: Boolean): Registration {
        return registrationDao.fetchLoggedInUser(loggedIn)
    }

    override suspend fun getRegistrationCount(): Int {
        return registrationDao.getRegistrationCount()
    }

}