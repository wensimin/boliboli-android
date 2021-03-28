package com.github.wensimin.boliboli_android.pojo

data class SimpleVoice(
    val id: String,
    val title: String,
    val rjId: String,
    val mainImg: String,
    val url: String,
    val r18: Boolean,
    val tags: List<SimpleVoiceTag>
)