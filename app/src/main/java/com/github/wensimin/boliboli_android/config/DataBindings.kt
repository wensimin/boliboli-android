package com.github.wensimin.boliboli_android.config

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target

@BindingAdapter("url")
fun setImage(view: ImageView, url: String?) {
    url?.let {
        // 加载header图
        Glide.with(view.context)
            .load(url).apply {
                override(Target.SIZE_ORIGINAL)
            }
            .into(view)
    }
}

