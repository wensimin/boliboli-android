package com.github.wensimin.boliboli_android.pojo.base

import java.util.*


open class Media(
    var filename: String,
    var type: MediaType,
    var size: Long,
    /**
     * 播放长度
     */
    var trackLength: Long? = null,
    id: String, createDate: Date, updateDate: Date,
) : Data(id, createDate, updateDate)

