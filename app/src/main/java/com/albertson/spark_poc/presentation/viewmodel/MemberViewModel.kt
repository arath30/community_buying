package com.albertson.spark_poc.presentation.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertson.spark_poc.core.state.MemberListState
import com.albertson.spark_poc.core.state.MemberInfoState
import com.albertson.spark_poc.core.utils.Results
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.domain.usecase.DeleteMemberUseCase
import com.albertson.spark_poc.domain.usecase.GetMemberInfoUseCase
import com.albertson.spark_poc.domain.usecase.InsertMemberUseCase
import com.albertson.spark_poc.domain.usecase.UpdateStatusMemberUseCase
import com.albertson.spark_poc.domain.usecases.GetMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val useCase: GetMemberUseCase,
    private val insertMemberUseCase: InsertMemberUseCase,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val deleterMemberUseCase: DeleteMemberUseCase,
    private val updateStatusMemberUseCase: UpdateStatusMemberUseCase
) : ViewModel() {

    private val _memberList = MutableStateFlow(MemberListState())

    var memberList: StateFlow<MemberListState> = _memberList.asStateFlow()

    private var _memberInfo = MutableStateFlow(MemberInfoState())
    var memberInfo: StateFlow<MemberInfoState> = _memberInfo.asStateFlow()

    val obj: ObservableBoolean = ObservableBoolean(false)

    var communityName = ""
    var communityId: Int? = 0


    fun getMembersList(communityId: Int) {
        useCase.invoke(communityId).onEach { result ->
            when (result) {
                is Results.Error -> {
                    Log.d("MemberViewModel", result.errorMessage)
                }

                Results.Loading -> {

                }

                is Results.Success -> {
                    _memberList.value = MemberListState(result.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getAllMemberListByEmailId(email: String, status: String): Member {
        return useCase.getAllMemberListByEmailId(email, status)
    }

    fun addMember(member: Member) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            insertMemberUseCase.addMember(member = member)
            member.communityId?.let { getMembersList(it) }
        }
    }

    suspend fun getMemberInfo(email: String) {
        getMemberInfoUseCase(email).onEach { result ->
            when (result) {
                is Results.Error -> {
                    Log.d("MemberViewModel", result.errorMessage)
                }

                Results.Loading -> {

                }

                is Results.Success -> {
                    _memberInfo.value = MemberInfoState(result.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteMember(member: Member) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            deleterMemberUseCase.deleteMember(member)
            member.communityId?.let { getMembersList(it) }
        }
    }

    fun acceptInvite(memberId: Int,status: String, rejectedBy: String) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            updateStatusMemberUseCase.updateMember(memberId, status, rejectedBy)
        }
    }

}