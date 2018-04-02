package com.kaibo.base

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author Administrator
 * @date 2018/4/2 0002 下午 3:36
 * GitHub：
 * email：
 * description：
 */
interface TestApi {

    @Streaming
    @GET
    fun downLoadFile(@Url url: String): Observable<ResponseBody>

}