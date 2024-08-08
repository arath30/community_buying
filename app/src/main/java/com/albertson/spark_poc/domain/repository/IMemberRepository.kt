package com.albertson.spark_poc.domain.repository

import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.data.local.entity.Registration
import kotlinx.coroutines.flow.Flow

interface IMemberRepository {
    fun getAllMemberList(communityId: Int): List<Member>
    suspend fun getAllMemberListByEmailId(email: String, status: String): Member
    suspend fun insertMember(member: Member)
    suspend fun getRegisteredUserInfo(email : String) : Member
    suspend fun deleteMember(member: Member)
    suspend fun updateMemberStatus(memberId: Int,status: String, rejectedBy: String)
}