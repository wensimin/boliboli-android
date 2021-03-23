package com.github.wensimin.boliboli_android.rest.dto

data class RestResponse<O>(val data: O? = null, val error: Exception? = null)