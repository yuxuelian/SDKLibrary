package com.kaibo.core.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import com.kaibo.core.service.FloatingService


/**
 * @author kaibo
 * @date 2018/6/20 10:55
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

const val REQUEST_OVERLAY_PERMISSION_CODE = 0x9999

fun Activity.startFloatingService() {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
        Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "package:${this.packageName}".toUri())
        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION_CODE)
    } else {
        //直接启动悬浮Service
        startService(Intent(this, FloatingService::class.java))
    }
}

fun Activity.checkOverlayPermission(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == REQUEST_OVERLAY_PERMISSION_CODE) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
            startService(Intent(this, FloatingService::class.java))
        }
    }
}