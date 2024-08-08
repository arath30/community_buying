package com.albertson.spark_poc.core.state

import com.albertson.spark_poc.data.local.entity.ProductCommunityUser

data class ProductCommUserState(
    val success: List<ProductCommunityUser>? = emptyList(),
    val error: String? = "",
    val isLoading: Boolean? = false
)