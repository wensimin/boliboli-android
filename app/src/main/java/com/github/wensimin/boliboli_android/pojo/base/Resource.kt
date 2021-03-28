package com.github.wensimin.boliboli_android.pojo.base

import java.util.*

/**
 * resource公用属性
 * resource 的定义为一个单独的资源，如音声&视频合集之类
 * 与media为 @oneToMany 关系
 */
open class Resource(
    /**
     * r18资源 需要权限
     */
    var r18: Boolean = false,
    id: String, createDate: Date, updateDate: Date,
) : Data(id, createDate, updateDate)