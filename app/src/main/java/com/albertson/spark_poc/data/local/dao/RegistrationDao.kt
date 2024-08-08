package com.albertson.spark_poc.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.albertson.spark_poc.data.local.entity.Registration

@Dao
interface RegistrationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegisteredUser(member: List<Registration>) : LongArray

    @Query("select * from Registration where email_address= :email")
    suspend fun getRegisteredUser(email: String) : Registration

    @Query("select * from Registration where usedId= :usedId")
    suspend fun getRegisteredUserById(usedId: Int) : Registration

    @Query("select * from Registration where logged_in= :loggedIn")
    suspend fun fetchLoggedInUser(loggedIn: Boolean) : Registration

    @Query("UPDATE Registration SET logged_in = :loggedIn WHERE email_address = :email_address")
    suspend fun updateRegisteredUserInfo(loggedIn: Boolean, email_address: String): Int

    @Query("SELECT COUNT(*) from Registration")
    suspend fun getRegistrationCount(): Int
}