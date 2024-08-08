package com.albertson.spark_poc.presentation.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertson.spark_poc.core.state.CommunityListState
import com.albertson.spark_poc.core.state.CommunityState
import com.albertson.spark_poc.core.utils.Results
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.domain.usecase.DeleteCommunityUseCase
import com.albertson.spark_poc.domain.usecase.InsertCommunityUseCase
import com.albertson.spark_poc.domain.usecase.UpdateCommunityActiveUseCase
import com.albertson.spark_poc.domain.usecases.GetCommunityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(private val getCommunityUseCase: GetCommunityUseCase,
    private val insertCommunityUseCase: InsertCommunityUseCase,
    private val updateCommunityActiveUseCase: UpdateCommunityActiveUseCase,
    private val deleteCommunityUseCase: DeleteCommunityUseCase) : ViewModel() {

    private val _communityName = MutableStateFlow(CommunityListState())
    private val _communityData = MutableStateFlow(CommunityState())
    var communityName : StateFlow<CommunityListState> = _communityName.asStateFlow()
    var communityData : StateFlow<CommunityState> = _communityData.asStateFlow()

    val obj: ObservableBoolean = ObservableBoolean(false)

    fun getCommunityList(userId: Int){
        getCommunityUseCase.invoke(userId).onEach { results ->
            when(results){

                is Results.Error -> {
                    Log.d("CommunityViewModel",results.errorMessage)
                }
                is Results.Success -> {
                    _communityName.value = CommunityListState(results.data)
                }

                Results.Loading -> {

                }
            }
        }.launchIn(viewModelScope)

    }

    fun getCommunityByCommunityId(communityId: Int) = viewModelScope.launch {
        getCommunityUseCase.invokeCommunity(communityId).onEach { results ->
            when(results){

                is Results.Error -> {
                    Log.d("CommunityViewModel",results.errorMessage)
                }
                is Results.Success -> {
                    _communityData.value = CommunityState(results.data)
                }

                Results.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    /*fun getAllCommunityByCommunityAdmin(communityAdmin: String) = viewModelScope.launch {
        getCommunityUseCase.invokeCommunityList(communityAdmin).onEach { results ->
            when(results){

                is Results.Error -> {
                    Log.d("CommunityViewModel",results.errorMessage)
                }
                is Results.Success -> {
                    _communityName.value = CommunityListState(results.data)
                }

                Results.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }*/



    fun insert(community: Community) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            insertCommunityUseCase.invoke(community = community)
            getCommunityList(community.userId)
            getCommunityByCommunityId(community.communityId)
        }
    }

    fun makeInActiveCommunity(community: Community) = viewModelScope.launch {
        withContext(Dispatchers.IO){
           updateCommunityActiveUseCase.makeInActiveCommunity(false,community.communityId)
            getCommunityList(community.userId)
            getCommunityByCommunityId(community.communityId)
        }
    }

    fun delete(community: Community, usedId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            deleteCommunityUseCase.deleteCommunity(community)
            getCommunityList(community.userId)
            getCommunityByCommunityId(community.communityId)
        }
    }




}