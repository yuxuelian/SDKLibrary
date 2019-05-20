package com.kaibo.core.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kaibo.core.R
import com.kaibo.core.error.handle
import com.kaibo.core.error.handleHttpException
import com.kaibo.core.http.DownLoadApi
import com.kaibo.core.http.progress.ProgressObservable
import com.kaibo.core.util.launchUI
import com.kaibo.core.util.installApk
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream

/**
 * @author kaibo
 * @date 2018/9/30 11:03
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class DownLoadApkService : Service() {

    companion object {
        private const val TAG = "DownLoadApkService"
        const val EXTRA_DOWNLOAD_URL = "downloadUrl"
        private const val NOTIFY_ID = 0x1234
    }

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private val notificationBuilder by lazy {
        // 通道的id
        val channelId = "scan_access_apk_download"
        //8.0需要设置通知栏通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, "下载", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icon_check_update)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentTitle("正在下载")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // apk存放目录(存放到外部存储,否则无法完成安装操作)
        val apkFile = File("${Environment.getExternalStorageDirectory().absolutePath}${File.separator}kaibo-access-update.apk")
        if (apkFile.exists()) {
            apkFile.delete()
        }
        apkFile.createNewFile()
        // 获取下载路劲
        val downloadUrl = intent?.getStringExtra(EXTRA_DOWNLOAD_URL) ?: ""
        // 监听下载进度
        val subscribe = ProgressObservable.listenerResponse(downloadUrl).subscribe({
            Log.d(TAG, "下载进度:it.rate=${it.rate}")
            // 发送进度到通知栏
            notifyProgress(100, (it.rate * 100).toInt())
        }) {
            if (it is HttpException) {
                handleHttpException(it)
            } else {
                handle(it)
            }
        }

        launchUI {
            try {
                val responseBody = DownLoadApi.instance.downLoadFileAsync(downloadUrl).await()
                // 保存文件
                responseBody.byteStream()?.use {
                    val fileOutputStream = FileOutputStream(apkFile)
                    it.copyTo(fileOutputStream)
                    fileOutputStream.close()
                }
                // 任务执行完成 取消通知栏
                notificationManager.cancel(NOTIFY_ID)
                // 下载完成  启动安装
                installApk(apkFile)
            } catch (e: Exception) {
                // 任务出错  取消通知栏
                notificationManager.cancel(NOTIFY_ID)
                if (e is HttpException) {
                    handleHttpException(e)
                } else {
                    handle(e)
                }
            }
            stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun notifyProgress(max: Int, progress: Int) {
        //构建通知   indeterminate 是否是模糊显示(不显示具体数值)
        notificationBuilder.setProgress(max, progress, false)
        // 发送到通知栏
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build())
    }
}
