package com.albertson.spark_poc.core.state

import com.albertson.spark_poc.data.local.entity.Community

data class CommunityState(
    val success: Community? = null,
    val error: String? = "",
    val isLoading: Boolean? = false
)