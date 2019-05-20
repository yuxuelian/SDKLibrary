package com.kaibo.core.activity

import android.os.Bundle
import com.kaibo.core.R
import com.kaibo.core.toast.showError
import com.kaibo.core.toast.showInfo
import com.kaibo.core.util.toDate
import com.orhanobut.logger.Logger
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.UpgradeInfo
import com.tencent.bugly.beta.download.DownloadListener
import com.tencent.bugly.beta.download.DownloadTask
import kotlinx.android.synthetic.main.activity_upgrade.*

/**
 * @author kaibo
 * @date 2018/12/3 16:30
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */
class UpgradeActivity : SuperActivity() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_upgrade
    }

    private fun Long.toMB(): String {
        return "${(this / 1024 / 10.24).toInt() / 100.0}MB"
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        updateBtn(Beta.getStrategyTask())
        val upgradeInfo: UpgradeInfo = Beta.getUpgradeInfo()
        // 标题
        title_tv.text = upgradeInfo.title
        // 下载进度
//        progress.text = Beta.getStrategyTask().savedLength.toMB()
        // 包版本名
        version.text = upgradeInfo.versionName
        // 包总大小
        size.text = upgradeInfo.fileSize.toMB()
        // 更新时间
        time.text = upgradeInfo.publishTime.toDate()
        // 更新内容
        content.text = upgradeInfo.newFeature
        // 点击启动按钮
        start.setOnClickListener {
            // 启动下载
            val task = Beta.startDownload()
            // 更新按钮文字
            updateBtn(task)
            if (task.status == DownloadTask.DOWNLOADING) {
                // 点击开始下载后关闭Activity
                finish()
            }
        }
        // 点击取消下载
        cancel.setOnClickListener {

            Beta.cancelDownload()
            finish()
        }

        Beta.downloadListener = object : DownloadListener {
            // 进度
            override fun onReceive(task: DownloadTask) {
                updateBtn(task)
//                tv.text = task.savedLength.toString()
                Logger.d(task.savedLength)
                showInfo("${task.savedLength} -")
            }

            override fun onCompleted(task: DownloadTask) {
                updateBtn(task)
//                tv.text = task.savedLength.toString()
                Logger.d(task.savedLength)
            }

            override fun onFailed(task: DownloadTask, code: Int, extMsg: String) {
//                tv.text = "failed"
                showError("下载失败")
                updateBtn(task)
                Logger.d(task.savedLength)
            }
        }
//        // 监听下载
//        Beta.registerDownloadListener(object : DownloadListener {
//            // 进度
//            override fun onReceive(task: DownloadTask) {
//                updateBtn(task)
////                tv.text = task.savedLength.toString()
//                Logger.d(task.savedLength)
//            }
//
//            override fun onCompleted(task: DownloadTask) {
//                updateBtn(task)
////                tv.text = task.savedLength.toString()
//                Logger.d(task.savedLength)
//            }
//
//            override fun onFailed(task: DownloadTask, code: Int, extMsg: String) {
////                tv.text = "failed"
//                showError("下载失败")
//                updateBtn(task)
//                Logger.d(task.savedLength)
//            }
//        })
    }

    override fun onDestroy() {
        // 取消监听
//        Beta.unregisterDownloadListener()
        super.onDestroy()
    }

    private fun updateBtn(task: DownloadTask) {
        when (task.status) {
            DownloadTask.INIT, DownloadTask.DELETED, DownloadTask.FAILED -> {
                start.text = "开始下载"
            }
            DownloadTask.COMPLETE -> {
                // 直接启动安装
                start.text = "安装"
            }
            DownloadTask.DOWNLOADING -> {
                start.text = "暂停"
            }
            DownloadTask.PAUSED -> {
                start.text = "继续下载"
            }
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
