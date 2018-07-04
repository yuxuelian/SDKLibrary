package com.kaibo.core.http

import com.kaibo.core.annotation.PoKo

/**
 * @author kaibo
 * @date 2018/7/2 18:14
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：data是数组的类型
 */

@PoKo
data class WrapListDataBean<T>(
        val code: Int,
        val msg: String,
        val data: List<T>
)