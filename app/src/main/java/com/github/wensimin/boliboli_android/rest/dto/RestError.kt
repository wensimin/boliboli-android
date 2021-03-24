package com.github.wensimin.boliboli_android.rest.dto

/**
 * rest Error
 */
data class RestError(
    /**
     *  error code
     */
    val error: String? = null,
    val message: String? = null
)