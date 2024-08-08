package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.domain.repository.ICommunityRepository
import com.albertson.spark_poc.domain.repository.IMemberRepository
import javax.inject.Inject

class UpdateCommunityActiveUseCase @Inject constructor(private val iCommunityRepository: ICommunityRepository) {
     suspend fun makeInActiveCommunity(isActive: Boolean, communityId: Int) {
        iCommunityRepository.makeInActiveCommunity(isActive,communityId)
    }
}