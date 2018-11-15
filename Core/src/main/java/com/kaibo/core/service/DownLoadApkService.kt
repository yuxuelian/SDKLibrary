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
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.kaibo.core.R
import com.kaibo.core.http.api.DownLoadApi
import com.kaibo.core.http.progress.ProgressObservable
import com.kaibo.core.utl.installApk
import io.reactivex.disposables.CompositeDisposable
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
        private const val notifyId = 0x1234
    }

    private val compositeDisposable: CompositeDisposable  by lazy { CompositeDisposable() }

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
                .setSmallIcon(R.drawable.update_icon)
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
        val apkFile = File("${Environment.getExternalStorageDirectory().absolutePath}${File.separator}yishi-access-update.apk")
        if (apkFile.exists()) {
            apkFile.delete()
        }
        apkFile.createNewFile()
        // 获取下载路劲
        val downloadUrl = intent?.getStringExtra("downloadUrl") ?: ""
        // 监听下载进度
        compositeDisposable.add(ProgressObservable.listener(downloadUrl).subscribe({
            Log.d(TAG, "下载进度:it.rate=${it.rate}")
            // 发送进度到通知栏
            notifyProgress(100, (it.rate * 100).toInt())
        }) {
            if (it is HttpException) {
//                ToastUtils.showError(it.errorBodyMsg())
            } else {
//                HandleException.handle(it)
                it.printStackTrace()
            }
        })

        // 执行下载 这里因为Retrofit创建的时候就默认指定了在子线程发出请求,所以不能使用IntentService同步执行
        compositeDisposable.add(DownLoadApi.instance.downLoadFile(downloadUrl).subscribe({ responseBody ->
            // 保存文件
            val inputStream = responseBody.byteStream()
            FileOutputStream(apkFile).use {
                inputStream.copyTo(it, 2048)
            }
            inputStream.close()

            // 下载完成  启动安装
            installApk(apkFile)

            // 任务执行完成 取消通知栏
            notificationManager.cancel(DownLoadApkService.notifyId)
            stopSelf()
        }) {
            // 任务出错  取消通知栏
            notificationManager.cancel(DownLoadApkService.notifyId)
            stopSelf()
            if (it is HttpException) {
//                ToastUtils.showError(it.errorBodyMsg())
            } else {
                it.printStackTrace()
//                HandleException.handle(it)
            }
        })
        return Service.START_NOT_STICKY
    }

    private fun notifyProgress(max: Int, progress: Int) {
        //构建通知   indeterminate 是否是模糊显示
        notificationBuilder.setProgress(max, progress, false)
        // 发送到通知栏
        notificationManager.notify(DownLoadApkService.notifyId, notificationBuilder.build())
    }

    override fun onDestroy() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        super.onDestroy()
    }
}