package com.github.wensimin.boliboli_android.pojo

import com.github.wensimin.boliboli_android.pojo.base.Resource
import java.util.*

class Voice(
    val title: String,
    val rjId: String,
    val mainImg: String,
    val url: String,
    val tags: List<SimpleVoiceTag>,
    r18: Boolean = false, id: String, createDate: Date, updateDate: Date
) : Resource(r18, id, createDate, updateDate)