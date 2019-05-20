package com.kaibo.core.error

import android.content.Context
import com.kaibo.core.http.errorBodyMsg
import com.kaibo.core.toast.showError
import com.orhanobut.logger.Logger
import kotlinx.serialization.json.JsonParsingException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownServiceException
import javax.net.ssl.SSLHandshakeException

/**
 * @author kaibo
 * @date 2018/10/25 17:50
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

fun Context.handle(throwable: Throwable) {
    throwable.printStackTrace()
    when (throwable) {
        is ConnectException -> {
            this.showError("网络连接异常")
        }
        is SocketException -> {

        }
        is UnknownServiceException -> {

        }
        is SocketTimeoutException -> {
            this.showError("服务器连接超时")
        }
        is JsonParsingException -> {
            this.showError("数据解析异常")
        }
        is SSLHandshakeException -> {
            this.showError("证书验证失败")
        }
        else -> {
        }
    }
}

fun Context.handleHttpException(e: HttpException) {
    when (e.code()) {
        in 500..599 -> {
            this.showError("内部服务器错误")
        }
        in 400..499 -> {
            e.errorBodyMsg()?.let { errorJson ->
                // 请求体中的内容
                Logger.d("errorJson = $errorJson")
                val jsonObject = JSONObject(errorJson)
                jsonObject.keys().forEach { key ->
                    val value = jsonObject.get(key)
                    if (value is String) {
                        showError(value)
                        // 结束函数
                        return
                    }
                }
            }
            showError("请求出错")
        }
    }
}