package com.albertson.spark_poc.domain.usecase

import com.albertson.spark_poc.domain.repository.IMemberRepository
import javax.inject.Inject

class UpdateStatusMemberUseCase @Inject constructor(private val iMemberRepository: IMemberRepository) {
    suspend fun updateMember(memberId: Int,status: String, rejectedBy: String){
        iMemberRepository.updateMemberStatus(memberId, status, rejectedBy)
    }
}