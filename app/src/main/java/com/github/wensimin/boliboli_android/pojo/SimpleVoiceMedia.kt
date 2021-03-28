package com.github.wensimin.boliboli_android.pojo

import com.github.wensimin.boliboli_android.pojo.base.MediaType

/**
 * voice media
 */
data class SimpleVoiceMedia(
    val id: String,
    val filename: String,
    val type: MediaType,
    val size: Long,
    val trackLength: Long?,
    var folder: String?
)