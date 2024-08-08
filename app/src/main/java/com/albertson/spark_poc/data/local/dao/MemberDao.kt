package com.albertson.spark_poc.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.albertson.spark_poc.data.local.entity.Member

@Dao
interface MemberDao {

    @Query("SELECT * FROM user WHERE community_id = :communityId")
    fun getAllMemberList(communityId: Int): List<Member>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member)

    @Query("select * from user where member_email= :email")
    suspend fun getLoggedInUserInfo(email: String): Member

    @Query("SELECT * FROM user WHERE rejected_to_user = :email AND status =:status")
    suspend fun getAllMemberListByEmailId(email: String, status: String): Member

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("UPDATE user SET status = :status, rejected_to_user =:rejectedBy  WHERE memberId = :memberId")
    suspend fun updateMemberStatus(memberId: Int,status: String, rejectedBy: String)
}