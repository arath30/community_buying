package com.albertson.spark_poc.core.state

import com.albertson.spark_poc.data.local.entity.Member

data class MemberListState(
    val success: List<Member>? = emptyList(),
    val error: String? = "",
    val isLoading: Boolean? = false
)