package com.albertson.spark_poc.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product_community_user")
data class ProductCommunityUser(
    @PrimaryKey(autoGenerate = true) val product_id: Int = 0,
    @ColumnInfo(name = "product_name") val product_name: String,
    @ColumnInfo(name = "product_image") val product_image: String,
    @ColumnInfo(name = "product_base_price") val product_base_price: String,
    @ColumnInfo(name = "product_discount_price") var product_discount_price: String,
    @ColumnInfo(name = "product_weight") val product_weight: String,
    @ColumnInfo(name = "product_quantity") var product_quantity: Int = 0,
    @ColumnInfo(name = "member_name") var member_name: String = "",
    @ColumnInfo(name = "member_id") var member_id: Int = 0,
    @ColumnInfo(name = "community_name") var community_name: String = "",
    @ColumnInfo(name = "community_id") var community_id: Int = 0,
    @ColumnInfo(name = "user_id") var userId: Int = 0
)
