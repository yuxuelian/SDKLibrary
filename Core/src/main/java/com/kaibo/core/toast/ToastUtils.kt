package com.kaibo.core.toast

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.kaibo.core.R
import kotlinx.android.synthetic.main.toast_layout.view.*
import java.lang.ref.WeakReference

/**
 * @author:Administrator
 * @date:2018/5/14 0014 上午 9:09
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

@ColorInt
private val DEFAULT_TEXT_COLOR = Color.WHITE

@DrawableRes
private val INFO_COLOR = R.drawable.corners_info_toast_bg

@DrawableRes
private val SUCCESS_COLOR = R.drawable.corners_success_toast_bg

@DrawableRes
private val WARNING_COLOR = R.drawable.corners_warning_toast_bg

@DrawableRes
private val ERROR_COLOR = R.drawable.corners_error_toast_bg

/**
 * 缓存上一次显示的Toast
 */
private var preToast: WeakReference<Toast>? = null

private fun Context.set(
        showIcon: Boolean,
        @ColorInt textColor: Int,
        @DrawableRes bgDrawable: Int,
        msg: CharSequence?,
        @DrawableRes icon: Int) {
    // 每次调用之前取消一下上一次的Toast
    preToast?.get()?.cancel()
    val mView: View = LayoutInflater.from(this).inflate(R.layout.toast_layout, null, false)
    preToast = WeakReference(Toast(this.applicationContext).apply {
        view = mView
        view.setBackgroundResource(bgDrawable)
        view.toast_icon.visibility = if (showIcon) View.VISIBLE else View.GONE
        view.toast_icon.setImageResource(icon)
        view.toast_text.setTextColor(textColor)
        view.toast_text.text = msg
        duration = Toast.LENGTH_SHORT
        show()
    })
}

@JvmOverloads
fun Context.showInfo(
        msg: CharSequence?,
        @ColorInt textColor: Int = DEFAULT_TEXT_COLOR,
        @DrawableRes tintColor: Int = INFO_COLOR,
        showIcon: Boolean = true) {
    this.set(showIcon, textColor, tintColor, msg, R.drawable.icon_info)
}

@JvmOverloads
fun Context.showSuccess(
        msg: CharSequence?,
        @ColorInt textColor: Int = DEFAULT_TEXT_COLOR,
        @DrawableRes tintColor: Int = SUCCESS_COLOR,
        showIcon: Boolean = true) {
    this.set(showIcon, textColor, tintColor, msg, R.drawable.icon_success)
}

@JvmOverloads
fun Context.showWarning(
        msg: CharSequence?,
        @ColorInt textColor: Int = DEFAULT_TEXT_COLOR,
        @DrawableRes tintColor: Int = WARNING_COLOR,
        showIcon: Boolean = true) {
    this.set(showIcon, textColor, tintColor, msg, R.drawable.icon_warning)
}

@JvmOverloads
fun Context.showError(
        msg: CharSequence?,
        @ColorInt textColor: Int = DEFAULT_TEXT_COLOR,
        @DrawableRes tintColor: Int = ERROR_COLOR,
        showIcon: Boolean = true) {
    this.set(showIcon, textColor, tintColor, msg, R.drawable.icon_error)
}
