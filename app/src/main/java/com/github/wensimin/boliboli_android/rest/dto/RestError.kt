package com.github.wensimin.boliboli_android.rest.dto

/**
 * rest Error
 */
data class RestError(
    /**
     * spring error 基本OAuth相关
     */
    val error: String? = null,
    /**
     *  是system内部给予的error code
     */
    val type: String? = null,
    val message: String? = null
)