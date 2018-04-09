package com.kaibo.base.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

/**
 * @author Administrator
 * @date 2018/4/8 0008 下午 12:52
 * GitHub：
 * email：
 * description：对android屏幕尺寸等相关操作的扩展方法
 */

fun Context.dip2px(dipValue: Float): Float {
    val scale: Float = resources.displayMetrics.density
    return dipValue * scale + 0.5f
}

/**
 *
 */
fun Context.px2dip(pxValue: Float): Int {
    val scale: Float = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 状态栏的高度
 */
val Context.statusBarHeight
    get() = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))

/**
 * 设置沉浸式
 * title  titleView   没有可以传null
 * isLight是否对状态栏颜色变黑
 */
fun Activity.immersive(title: View?, isLight: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        with(window) {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    if (isLight && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        0
                    }
            statusBarColor = Color.TRANSPARENT
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    title?.setPadding(0, this.statusBarHeight, 0, 0)
}

fun Fragment.immersiveTitleView(view: View) {
    context?.let {
        view.setPadding(0, it.statusBarHeight, 0, 0)
    }
}

fun ViewGroup.inflate(layoutRes: Int): View? {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

/**
 * ImageView使用Glide进行图片加载
 */
//fun ImageView.defaultLoadImage(url: String?) {
//    GlideApp
//            .with(context)
//            .load(url)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .centerCrop()
//            .placeholder(R.drawable.ic_image_loading)
//            .error(R.drawable.ic_empty_picture)
//            .into(this)
//}
//
//fun ImageView.displayHigh(url: String?) {
//    GlideApp
//            .with(context)
//            .asBitmap()
//            .load(url)
//            .format(DecodeFormat.PREFER_ARGB_8888)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .centerCrop()
//            .placeholder(R.drawable.ic_image_loading)
//            .error(R.drawable.ic_empty_picture)
//            .into(this)
//}