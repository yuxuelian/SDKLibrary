package com.kaibo.core.http.progress

/**
 * @author:Administrator
 * @date:2018/5/31 0031下午 5:48
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

data class ProgressMessage(
        val currentLength: Long,
        val fillLength: Long,
        val done: Boolean = false
) {
    /**
     * 这个比例只有在使用的时候才进行计算,不使用的话不必计算
     */
    val rate by lazy {
        currentLength / fillLength.toDouble()
    }
}