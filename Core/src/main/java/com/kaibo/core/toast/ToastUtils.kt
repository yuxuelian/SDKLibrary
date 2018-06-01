package com.kaibo.core.toast

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.kaibo.core.R
import kotlinx.android.synthetic.main.toast_layout.view.*


/**
 * @author:Administrator
 * @date:2018/5/14 0014 上午 9:09
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

object ToastUtils {

    private lateinit var toast: Toast

    @SuppressLint("ShowToast")
    fun init(context: Context) {
        toast = Toast(context)
        toast.view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null, false)
    }

    @ColorInt
    private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

    @ColorInt
    private val INFO_COLOR = Color.parseColor("#3F51B5")

    @ColorInt
    private val SUCCESS_COLOR = Color.parseColor("#388E3C")

    @ColorInt
    private val WARNING_COLOR = Color.parseColor("#FFA900")

    @ColorInt
    private val ERROR_COLOR = Color.parseColor("#FD4C5B")

    @JvmOverloads
    fun showInfo(msg: String, @ColorInt textColor: Int = DEFAULT_TEXT_COLOR, @ColorInt tintColor: Int = INFO_COLOR, showIcon: Boolean = true) {
        set(tintColor, showIcon, textColor, msg, R.drawable.icon_info)
    }

    @JvmOverloads
    fun showSuccess(msg: String, @ColorInt textColor: Int = DEFAULT_TEXT_COLOR, @ColorInt tintColor: Int = SUCCESS_COLOR, showIcon: Boolean = true) {
        set(tintColor, showIcon, textColor, msg, R.drawable.icon_success)
    }

    @JvmOverloads
    fun showWarning(msg: String, @ColorInt textColor: Int = DEFAULT_TEXT_COLOR, @ColorInt tintColor: Int = WARNING_COLOR, showIcon: Boolean = true) {
        set(tintColor, showIcon, textColor, msg, R.drawable.icon_warning)
    }

    @JvmOverloads
    fun showError(msg: String, @ColorInt textColor: Int = DEFAULT_TEXT_COLOR, @ColorInt tintColor: Int = ERROR_COLOR, showIcon: Boolean = true) {
        set(tintColor, showIcon, textColor, msg, R.drawable.icon_error)
    }

    private fun set(@ColorInt tintColor: Int, showIcon: Boolean, @ColorInt textColor: Int, msg: String, @DrawableRes icon: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toast.view.backgroundTintList = ColorStateList.valueOf(tintColor)
        }
        toast.view.toastIcon.visibility = if (showIcon) View.VISIBLE else View.GONE
        toast.view.toastIcon.setImageResource(icon)
        toast.view.toastText.setTextColor(textColor)
        toast.view.toastText.text = msg
        toast.show()
    }
}