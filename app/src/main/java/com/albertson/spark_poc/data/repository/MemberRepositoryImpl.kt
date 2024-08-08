package com.albertson.spark_poc.data.repository

import com.albertson.spark_poc.data.local.dao.MemberDao
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.domain.repository.IMemberRepository
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(private val memberDao: MemberDao):
    IMemberRepository {
    override fun getAllMemberList(communityId: Int): List<Member> {
        return memberDao.getAllMemberList(communityId = communityId)
    }

    override suspend fun getAllMemberListByEmailId(email: String, status: String): Member {
        return memberDao.getAllMemberListByEmailId(email = email, status = status)
    }

    override suspend fun insertMember(member: Member) {
        memberDao.insertMember(member)
    }

    override suspend fun getRegisteredUserInfo(email: String): Member {
        return memberDao.getLoggedInUserInfo(email)
    }

    override suspend fun deleteMember(member: Member) {
        memberDao.deleteMember(member)
    }

    override suspend fun updateMemberStatus(memberId: Int,status: String, rejectedBy: String) {
        memberDao.updateMemberStatus(memberId, status, rejectedBy)
    }
}