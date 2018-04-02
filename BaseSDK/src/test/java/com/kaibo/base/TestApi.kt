package com.kaibo.base

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET

/**
 * @author Administrator
 * @date 2018/4/2 0002 下午 3:36
 * GitHub：
 * email：
 * description：
 */
interface TestApi {

    @GET("https://qd.myapp.com/myapp/qqteam/Androidlite/qqlite_3.6.3.697_android_r110028_GuanWang_537055374_release_10000484.apk")
    fun downLoadFile(): Observable<ResponseBody>

}