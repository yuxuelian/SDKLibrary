package com.kaibo.core.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable as KSerializable

/**
 * @author kaibo
 * @date 2018/6/28 18:31
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：Data是Object
 */

@KSerializable
data class BaseBean<T>(
        @SerialName("code") val code: Int,
        @SerialName("msg") val msg: String,
        @SerialName("data") val data: T
)
