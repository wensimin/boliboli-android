package com.github.wensimin.boliboli_android.rest.pojo

data class RestResponse<O>(val data: O? = null, val error: Exception? = null)