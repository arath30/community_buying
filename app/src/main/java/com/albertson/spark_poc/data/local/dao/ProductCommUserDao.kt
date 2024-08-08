package com.albertson.spark_poc.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser

@Dao
interface ProductCommUserDao {
    @Query("SELECT * FROM product_community_user WHERE community_id = :communityId")
    fun getAllCommunityList(communityId: Int): List<ProductCommunityUser>

    @Query("SELECT * FROM product_community_user WHERE community_name = :communityName AND user_id = :userId")
    fun getProductCommUserListByUserId(communityName: String, userId: Int): List<ProductCommunityUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productCommunityUser: ProductCommunityUser)

    @Query("DELETE FROM product_community_user WHERE product_id = :productId")
    fun deleteProductFromList(productId: Int)

    @Query("UPDATE product_community_user SET product_quantity = :quantity WHERE product_id = :productId")
    fun updateProduct(productId: Int, quantity: Int)

}