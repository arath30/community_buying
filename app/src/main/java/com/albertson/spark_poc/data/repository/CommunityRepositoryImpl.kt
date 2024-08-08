package com.albertson.spark_poc.data.repository

import com.albertson.spark_poc.data.local.dao.CommunityDao
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.domain.repository.ICommunityRepository
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(private val communityDao: CommunityDao) :
    ICommunityRepository {
    override fun getCommunityList(userId: Int): List<Community> {
        return communityDao.getAllCommunityListByUserId(userId)
    }

    override fun getAllCommunityByCommunityId(communityId: Int): Community {
        return communityDao.getAllCommunityByCommunityId(communityId)
    }

    override fun getAllCommunityByCommunityAdmin(communityAdmin: String): List<Community> {
        return communityDao.getAllCommunityByCommunityAdmin(communityAdmin)
    }

    override suspend fun insertCommunity(community: Community) {
        communityDao.insert(community)
    }

    override suspend fun makeInActiveCommunity(isActive: Boolean, communityId: Int) {
        communityDao.makeInActiveCommunity(isActive,communityId)
    }

    override suspend fun deleteCommunity(community: Community){
        communityDao.deleteCommunity(community)
    }


}