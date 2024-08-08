package com.albertson.spark_poc.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community")
data class Community(@PrimaryKey(autoGenerate = true) val communityId: Int = 0,
                     @ColumnInfo(name = "community_name") val communityName: String,
                     @ColumnInfo(name = "user_id") val userId: Int,
                     @ColumnInfo(name = "community_admin") val communityAdmin: String,
                     @ColumnInfo(name = "status") val status: String,
                     @ColumnInfo(name = "is_active") val isActive: Boolean
                    )

