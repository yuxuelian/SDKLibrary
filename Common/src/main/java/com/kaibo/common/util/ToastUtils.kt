package com.kaibo.common.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import com.kaibo.common.BaseApplication

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:48
 * GitHub：
 * email：
 * description：
 */
object ToastUtils {

    private lateinit var toast: Toast

    @SuppressLint("ShowToast")
    fun init(context: Context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
    }

    fun showToast(msg: String) {
        with(toast) {
            setText(msg)
            show()
        }
    }
}