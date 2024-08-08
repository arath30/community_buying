package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.domain.repository.ICommunityRepository
import com.albertson.spark_poc.domain.repository.IMemberRepository
import javax.inject.Inject

class DeleteCommunityUseCase @Inject constructor(private val iCommunityRepository: ICommunityRepository) {
     suspend fun deleteCommunity(community: Community) {
        iCommunityRepository.deleteCommunity(community)
    }
}