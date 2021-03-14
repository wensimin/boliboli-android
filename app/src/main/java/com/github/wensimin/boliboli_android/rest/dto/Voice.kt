package com.github.wensimin.boliboli_android.rest.dto

data class Voice(
    val title: String,
    val rjId: String,
    val mainImg: String,
    val url: String,
    val tags: List<VoiceTag>,
    var r18: Boolean = false,
    var fileTree: String = ""
)