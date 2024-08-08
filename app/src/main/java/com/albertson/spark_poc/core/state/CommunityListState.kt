package com.albertson.spark_poc.core.state

import com.albertson.spark_poc.data.local.entity.Community

data class CommunityListState(
    val success: List<Community>? = emptyList(),
    val error: String? = "",
    val isLoading: Boolean? = false
)