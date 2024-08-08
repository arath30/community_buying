package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.domain.repository.ICommunityRepository
import com.albertson.spark_poc.domain.repository.IMemberRepository
import javax.inject.Inject

class InsertCommunityUseCase @Inject constructor(private val iCommunityRepository: ICommunityRepository) {
    suspend operator fun invoke(community: Community){
        iCommunityRepository.insertCommunity(community)
    }
}