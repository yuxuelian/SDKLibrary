package com.kaibo.core.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView


/**
 * @author kaibo
 * @date 2018/6/20 10:58
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class FloatingService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showFloatingWindow()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showFloatingWindow() {
        // 获取WindowManager服务
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // 新建悬浮窗控件
        val button = TextView(applicationContext)
        button.text = "Floating Window"
        //关闭大写转换
        button.setAllCaps(false)

        // 设置LayoutParam
        val layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.width = 50
        layoutParams.height = 50
        layoutParams.gravity = Gravity.CENTER
        // 将悬浮窗控件添加到WindowManager
        windowManager.addView(button, layoutParams)
    }

}