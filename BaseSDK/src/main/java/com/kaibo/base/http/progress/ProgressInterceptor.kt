package com.kaibo.base.http.progress

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Administrator
 * @date 2018/4/2 0002 下午 3:28
 * GitHub：
 * email：
 * description：
 */
class ProgressInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder().body(ProgressResponseBody(response.body()!!, { progress, isFinish ->

            println("${Thread.currentThread().name} $progress $isFinish")

        })).build()
    }
}