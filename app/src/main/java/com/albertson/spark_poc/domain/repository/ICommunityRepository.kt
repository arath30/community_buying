package com.albertson.spark_poc.domain.repository

import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.data.local.entity.Member

interface ICommunityRepository {
    fun getCommunityList(userId: Int) : List<Community>
    fun getAllCommunityByCommunityId(communityId: Int) : Community
    fun getAllCommunityByCommunityAdmin(communityAdmin: String) : List<Community>
    suspend fun insertCommunity(community: Community)
    suspend fun makeInActiveCommunity(isActive: Boolean,communityId: Int)
    suspend fun deleteCommunity(community: Community)
}