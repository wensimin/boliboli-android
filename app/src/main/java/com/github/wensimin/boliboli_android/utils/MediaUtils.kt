package com.github.wensimin.boliboli_android.utils

import java.time.Duration

fun Long.toMinuteString(): String {
    return Duration.ofMillis(this).let {
        if (it.toMinutes() > 60) {
            val hoursPart = it.toHours()
            "${hoursPart}时${it.minusHours(hoursPart).toMinutes()}分"
        } else {
            val minutePart = it.toMinutes()
            "${minutePart}分${it.minusMinutes(minutePart).seconds}秒"
        }
    }
}

fun Long.toSizeString(): String {
    val fileMB = this / (1024 * 1024)
    val fileGB = fileMB / 1024
    return if (fileGB > 0L) "${fileGB}G" else "${fileMB}M"
}