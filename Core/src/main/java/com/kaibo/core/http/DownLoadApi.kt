package com.kaibo.core.http

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author kaibo
 * @date 2018/11/15 14:35
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

interface DownLoadApi {

    companion object {
        val instance: DownLoadApi by lazy {
            HttpRequestManager.retrofit.create(DownLoadApi::class.java)
        }
    }

    @Streaming
    @GET
    fun downLoadFileAsync(@Url url: String): Deferred<ResponseBody>

}