package com.kaibo.core.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.v4.app.Fragment
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.kaibo.core.BaseApplication

/**
 * @author:Administrator
 * @date:2018/4/8 0008 下午 12:52
 * GitHub:
 * email:
 * description:对android屏幕尺寸等相关操作的扩展方法
 */

/**
 * 状态栏的高度
 */
val Activity.statusBarHeight
    get() = BaseApplication.statusBarHeight
val Fragment.statusBarHeight
    get() = BaseApplication.statusBarHeight
val View.statusBarHeight
    get() = BaseApplication.statusBarHeight

/**
 * 屏幕的宽高
 */
val Activity.deviceWidth
    get() = BaseApplication.deviceWidth

val Activity.deviceHeight
    get() = BaseApplication.deviceHeight

val Fragment.deviceWidth
    get() = BaseApplication.deviceWidth

val Fragment.deviceHeight
    get() = BaseApplication.deviceHeight

val View.deviceWidth
    get() = BaseApplication.deviceWidth

val View.deviceHeight
    get() = BaseApplication.deviceHeight

/**
 * 设置沉浸式
 * isLight是否对状态栏颜色变黑
 */
fun Window.immersive(isLight: Boolean) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
            //清除状态栏默认状态
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 布局设置为全屏布局
            //SYSTEM_UI_FLAG_LAYOUT_STABLE
            //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    if (isLight && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        0
                    }
            statusBarColor = Color.TRANSPARENT
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
            this.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        else -> {
            return
        }
    }
}

/**
 * 设置标题栏字体颜色
 */
fun Activity.setLightStatusBar(isLight: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (isLight) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        }
    }
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