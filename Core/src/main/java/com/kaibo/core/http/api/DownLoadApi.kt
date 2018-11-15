package com.kaibo.core.http.api

import com.kaibo.core.http.HttpRequestManager
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author kaibo
 * @date 2018/10/30 12:23
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

interface DownLoadApi {

    companion object {
        val instance = HttpRequestManager.retrofit.create(DownLoadApi::class.java)
    }

    @Streaming
    @GET
    fun downLoadFile(@Url url: String): Observable<ResponseBody>

    // 在线二维码生成接口
//    http://apis.juhe.cn/qrcode/api?key=78525c87bf3c5a02fb95981108908d6a&el=h&type=2&bgcolor=123123123&fgcolor=000000&w=300&m=10&text=%E4%BD%A0%E5%A5%BD
//    http://qr.liantu.com/api.php?text=

}