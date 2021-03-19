package com.github.wensimin.boliboli_android.rest.dto

data class Voice(
    val id: String,
    val title: String,
    val rjId: String,
    val mainImg: String,
    val url: String,
    val tags: List<VoiceTag>,
    val r18: Boolean = false,
    val fileTree: String = ""
)