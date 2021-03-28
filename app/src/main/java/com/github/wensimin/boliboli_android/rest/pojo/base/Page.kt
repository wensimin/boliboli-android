package com.github.wensimin.boliboli_android.rest.pojo.base

data class Page<T>(
    var content: List<T>,
    val totalPages: Int,
    val totalElements: Int,
    val last: Boolean,
    val first: Boolean,
    val number: Int,
    val size: Int
)