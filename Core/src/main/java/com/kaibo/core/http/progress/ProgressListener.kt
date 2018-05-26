package com.kaibo.core.http.progress

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Administrator
 * @date 2018/4/3 0003 上午 9:34
 * GitHub：
 * email：
 * description：
 */

object ProgressListener{
    /**
     * 监听下载进度
     */
    val downloadProgressListeners by lazy {
        ConcurrentHashMap<String, (currentLength: Long, fillLength: Long, done: Boolean) -> Unit>()
    }

    /**
     * 监听上传进度
     */
    val uploadProgressListener by lazy {
        ConcurrentHashMap<String, (currentLength: Long, fillLength: Long, done: Boolean) -> Unit>()
    }
}


