package com.albertson.spark_poc.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class Member(@PrimaryKey(autoGenerate = true) val memberId: Int = 0,
                  @ColumnInfo(name = "first_name") val firstName: String,
                  @ColumnInfo(name = "last_name") val lastName: String,
                  @ColumnInfo(name = "member_email") val email: String,
                  @ColumnInfo(name = "community_name") val communityName: String,
                  @ColumnInfo(name = "community_id") val communityId: Int?,
                  @ColumnInfo(name = "status") var status: String? = "Pending",
                  @ColumnInfo(name = "usedId") val userId: Int?,
                  @ColumnInfo(name = "rejected_to_user") var rejectedToUser: String? = null,
                  @ColumnInfo(name = "user_role") val userRole: String = "Member",
                  @ColumnInfo(name = "is_active") val isActive: Boolean
    )
