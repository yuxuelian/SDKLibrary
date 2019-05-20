package com.kaibo.core.http.interceptor

import com.kaibo.core.http.body.ProgressRequestBody
import com.kaibo.core.http.body.ProgressResponseBody
import okhttp3.*

/**
 * @author:Administrator
 * @date:2018/4/2 0002 下午 3:28
 * GitHub:
 * email:
 * description:
 */
class ProgressInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        return request.body()?.let { requestBody: RequestBody ->
            // 使用 url 作为 key 标记请求
            val url = request.url().toString()
            // 包装请求体
            val newRequest: Request = request.newBuilder().post(ProgressRequestBody(url, requestBody)).build()
            // 请求
            val response: Response = chain.proceed(newRequest)
            // 获得响应体
            response.body()?.let { responseBody: ResponseBody ->
                // 包装响应体
                response
                        .newBuilder()
                        .body(ProgressResponseBody(url, responseBody))
                        .build()
            }
        } ?: chain.proceed(request)
    }

}