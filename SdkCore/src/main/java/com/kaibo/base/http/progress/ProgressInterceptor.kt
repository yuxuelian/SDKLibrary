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
object ProgressInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()
        val response = chain.proceed(request)
        response.body()?.let {
            return response.newBuilder().body(ProgressResponseBody(url, it)).build()
        }
        return response
    }
}