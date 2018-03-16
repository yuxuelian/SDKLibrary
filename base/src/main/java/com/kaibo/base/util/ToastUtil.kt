package com.kaibo.base.util

import android.widget.Toast
import com.kaibo.base.BaseApplication

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:48
 * GitHub：
 * email：
 * description：
 */
object ToastUtil {

    private val toast = Toast.makeText(BaseApplication.INSTANCE, "", Toast.LENGTH_SHORT)

    fun showToast(msg: String) {
        toast.apply {
            setText(msg)
            show()
        }
    }
}