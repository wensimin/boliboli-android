package com.github.wensimin.boliboli_android.utils

import android.view.LayoutInflater
import android.view.View

fun LayoutInflater.inflateNoParent(id: Int): View = inflate(id, null, false)

