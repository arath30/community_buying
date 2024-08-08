package com.albertson.spark_poc.core.state

data class CreateAccountState(val success : String? = "",val error : String? = "", val isLoading : Boolean? = false)