package com.albertson.spark_poc.core.state

import com.albertson.spark_poc.data.local.entity.Member

data class MemberInfoState(val success: Member?=null,
                           val error: String? = "",
                           val isLoading: Boolean? = false)