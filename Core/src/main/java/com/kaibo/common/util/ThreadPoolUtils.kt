package com.kaibo.common.util

import java.util.concurrent.*

/**
 * @author Administrator
 * @date 2018/5/14 0014 下午 3:24
 * @GitHub：https://github.com/yuxuelian
 * @email：
 * @description：
 */

const val MAX_POOL_SIZ = 50
const val CORE_POOL_SIZE = 1
const val KEEP_ALIVE_TIME = 60L

/**
 * 全局线程池
 */
val executorService: ExecutorService by lazy {
    ThreadPoolExecutor(CORE_POOL_SIZE,
            MAX_POOL_SIZ,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            SynchronousQueue(),
            threadFactory("THREAD-KAIBO", false))
}

private fun threadFactory(name: String, daemon: Boolean) = ThreadFactory {
    val result = Thread(it, name)
    result.isDaemon = daemon
    result
}