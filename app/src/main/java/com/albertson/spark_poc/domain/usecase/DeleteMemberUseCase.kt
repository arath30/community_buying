package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.domain.repository.IMemberRepository
import javax.inject.Inject

class DeleteMemberUseCase @Inject constructor(private val iMemberRepository: IMemberRepository) {
    suspend fun deleteMember(member: Member){
        iMemberRepository.deleteMember(member)
    }
}