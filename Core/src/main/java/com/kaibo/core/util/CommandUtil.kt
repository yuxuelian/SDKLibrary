package com.kaibo.core.util

import android.annotation.SuppressLint
import java.io.BufferedInputStream

/**
 * @author kaibo
 * @date 2018/6/20 10:17
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */
object CommandUtil {

    @SuppressLint("PrivateApi")
    fun getProperty(propName: String): String? {
        return Class
                .forName("android.os.SystemProperties")
                .getMethod("get", String::class.java)
                .invoke(null, propName) as? String
    }

    fun exec(command: String): String {
        val process = Runtime.getRuntime().exec(command)
        val bufferedInputStream = BufferedInputStream(process.inputStream)
//        return String(bufferedInputStream.readBytes(bufferedInputStream.available()), Charset.forName("gb2312"))
        return String(bufferedInputStream.readBytes(bufferedInputStream.available()), Charsets.UTF_8)
    }
}
