package com.albertson.spark_poc.core.state

import android.graphics.drawable.Drawable

data class AccountState(
    val icon: Drawable? = null,
    val title: String? = "",
    val subTitle: String? = "",
    val enable: Boolean = false
)