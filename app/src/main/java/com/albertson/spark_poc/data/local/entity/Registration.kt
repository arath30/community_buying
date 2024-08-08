package com.albertson.spark_poc.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Registration")
data class Registration(
    @PrimaryKey(autoGenerate = true) val usedId: Int = 0,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "email_address") val emailAddress: String,
    @ColumnInfo(name = "logged_in") val loggedIn: Boolean? = false,
    @ColumnInfo(name = "user_role") val userRole: String,
    @ColumnInfo(name = "memberId") val memberId: Int = 0
)