package com.github.wensimin.boliboli_android.rest.dto

data class Voice(
    val title: String,
    val RJId: String,
    val mainImg: String,
    val tags: Set<VoiceTag>
)