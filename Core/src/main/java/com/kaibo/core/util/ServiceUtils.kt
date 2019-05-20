package com.kaibo.core.util

import android.app.Service
import android.content.Context
import com.kaibo.core.util.createIntent

/**
 * @author kaibo
 * @date 2018/10/29 11:09
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

inline fun <reified T : Service> Context.startService(vararg params: Pair<String, Any?>) =
        startActivity(createIntent(T::class.java, params))