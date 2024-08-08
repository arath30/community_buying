package com.albertson.spark_poc.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertson.spark_poc.core.state.RegisteredUserInfoState
import com.albertson.spark_poc.core.utils.Results
import com.albertson.spark_poc.data.local.entity.Registration
import com.albertson.spark_poc.domain.usecase.FetchLoggedInUserUseCase
import com.albertson.spark_poc.domain.usecase.GetRegisteredUserInfoUseCase
import com.albertson.spark_poc.domain.usecase.GetRegistrationUserCountUseCase
import com.albertson.spark_poc.domain.usecase.RegisteredUserUseCase
import com.albertson.spark_poc.domain.usecase.UpdateRegisteredUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisteredUserViewModel @Inject constructor(
    private val registeredUserUseCase: RegisteredUserUseCase,
    private val getRegisteredUserInfoUseCase: GetRegisteredUserInfoUseCase,
    private val updateRegisteredUserInfo: UpdateRegisteredUserInfoUseCase,
    private val fetchLoggedInUserUseCase: FetchLoggedInUserUseCase,
    private val getRegistrationUserCountUseCase: GetRegistrationUserCountUseCase,
) : ViewModel() {

    private var _registeredUserInfo = MutableLiveData<RegisteredUserInfoState>()
    val registeredUserInfo: LiveData<RegisteredUserInfoState> = _registeredUserInfo


    fun insertUser(registration: List<Registration>) {
        viewModelScope.launch {
            registeredUserUseCase(registration)
        }

    }

    suspend fun getUserCount(): Int {

        return getRegistrationUserCountUseCase.invoke()

    }

    fun updateUser(loggedIn: Boolean, email_address: String) {
        viewModelScope.launch {
            updateRegisteredUserInfo.invoke(loggedIn, email_address)
        }

    }

    suspend fun fetchLoggedInUser(loggedIn: Boolean): Registration {
        return fetchLoggedInUserUseCase(loggedIn)
    }

    suspend fun fetchLoggedInUserById(userId: Int): Registration {
        return fetchLoggedInUserUseCase(userId)
    }

    suspend fun fetchRegisteredUserInfo(email: String) {
        getRegisteredUserInfoUseCase.invoke(email).onEach {
            when (it) {
                is Results.Loading -> {
                    _registeredUserInfo.value = RegisteredUserInfoState(isLoading = true)
                }

                is Results.Success -> {
                    _registeredUserInfo.value = RegisteredUserInfoState(success = it.data)
                }

                is Results.Error -> {
                    _registeredUserInfo.value = RegisteredUserInfoState(error = it.errorMessage)
                }
            }

        }.launchIn(viewModelScope)

    }
}