package com.kaibo.common.util

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.kaibo.common.glide.GlideApp

/**
 * @author Administrator
 * @date 2018/2/22 0022 上午 9:44
 * GitHub：
 * email：
 * description：
 */

@BindingAdapter("glide_load_img")
fun glideLoadImg(iv: ImageView, url: String?) {
    GlideApp.with(iv.context)
            .load(url)
            .into(iv)
}