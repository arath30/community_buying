package com.albertson.spark_poc.domain.repository

import com.albertson.spark_poc.data.local.entity.Registration

interface IRegistration {
   suspend fun insertRegisteredUserInfo(registration: List<Registration>)
   suspend fun getRegisteredUserInfo(email : String) : Registration
   suspend fun getRegisteredUserById(userId : Int) : Registration
   suspend fun updateRegisteredUser(loggedIn: Boolean, email_address: String)
   suspend fun fetchLoggedInUser(loggedIn: Boolean): Registration
   suspend fun getRegistrationCount(): Int
}