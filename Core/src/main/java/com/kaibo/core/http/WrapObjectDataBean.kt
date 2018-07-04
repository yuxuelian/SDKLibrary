package com.kaibo.core.http

import com.kaibo.core.annotation.PoKo


/**
 * @author kaibo
 * @date 2018/6/28 18:31
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：Data是Object
 */

@PoKo
data class WrapObjectDataBean<T>(
        val code: Int,
        val msg: String,
        val data: T
)