package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.domain.repository.IMemberRepository
import javax.inject.Inject

class InsertMemberUseCase @Inject constructor(private val iMemberRepository: IMemberRepository) {
    suspend fun addMember(member: Member){
        iMemberRepository.insertMember(member)
    }
}