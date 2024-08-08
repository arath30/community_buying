package com.albertson.spark_poc.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityDao {

    @Query("SELECT * FROM community WHERE user_id = :userId")
    fun getAllCommunityListByUserId(userId: Int): List<Community>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(community: Community)

    @Query("SELECT * FROM community WHERE communityId = :communityId")
    fun getAllCommunityByCommunityId(communityId: Int): Community

    @Query("SELECT * FROM community WHERE community_admin = :communityAdmin")
    fun getAllCommunityByCommunityAdmin(communityAdmin: String): List<Community>


    @Query("UPDATE community SET is_active = :isActive WHERE communityId = :communityId")
    suspend fun makeInActiveCommunity(isActive: Boolean,communityId: Int)

    @Delete
    suspend fun deleteCommunity(community: Community)
}