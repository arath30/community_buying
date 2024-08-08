package com.albertson.spark_poc.core.state

import com.albertson.spark_poc.data.local.entity.Registration

data class RegisteredUserInfoState(val success : Registration? = null,val error : String? ="", val isLoading : Boolean? = false)