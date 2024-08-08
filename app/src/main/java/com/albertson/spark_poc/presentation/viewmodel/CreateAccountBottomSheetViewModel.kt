package com.albertson.spark_poc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.albertson.spark_poc.core.state.CreateAccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateAccountBottomSheetViewModel @Inject constructor(): ViewModel() {

    private val _userName = MutableStateFlow<String?>(null)
    val userName : StateFlow<String?> = _userName.asStateFlow()

    fun createAccount(username : String){
        _userName.value = username
    }

}