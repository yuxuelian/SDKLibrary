package com.kaibo.common.http

import com.kaibo.common.BaseApplication
import com.kaibo.common.http.progress.ProgressInterceptor
import com.kaibo.common.util.isNotNull
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * @author Administrator
 * @date 2018/3/28 0028 上午 11:21
 * GitHub：
 * email：
 * description：
 */
object HttpRequestManager {

    private const val BASE_URL = "http://localhost:8080/"

    //缓存大小   20M
    private const val CACHE_SIZE = 1024 * 1024 * 20L

    //连接超时时间
    private const val CONNECT_TIMEOUT_TIME = 30L
    //读超时时间
    private const val READ_TIMEOUT_TIME = 30L
    //写超时时间
    private const val WRITE_TIMEOUT_TIME = 30L

    //上传图片
    val IMAGE_MEDIA_TYPE = MediaType.parse("image/png")
    //上传json
    val JSON = MediaType.parse("application/json; charset=utf-8")
    //普通文本
    val TEXT = MediaType.parse("text/plain; charset=utf-8")
    //文件
    val FORM_DATA = MediaType.parse("multipart/form-data")

    /**
     * 全局唯一一个 OkHttpClient  实例
     */
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient
                .Builder()
                //进度拦截器
                .addInterceptor(ProgressInterceptor)
//            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                //失败重连
                .retryOnConnectionFailure(false)
                .connectTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_TIME, TimeUnit.SECONDS)
//           .cache(Cache(File("${BaseApplication.INSTANCE.cacheDir.absolutePath}${File.separator}okHttpCaches"), CACHE_SIZE))
                .build()
    }

    /**
     * Retrofit   实例
     */
    val retrofit: Retrofit by lazy {
        Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
//                .addConverterFactory(FastJsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                //同步发出请求
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //使用okhttp的线程池
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                //指定在RxJava的线程池发出请求
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    /**
     * 将路径列表转换成  List<MultipartBody.Part>
     *  适用于后台一个key接收文件数组的情况
     */
    fun filesToMultiBodyParts(key: String, flies: List<String>, mediaType: MediaType?): List<MultipartBody.Part> {
        val tempMediaType = checkNotNull(mediaType)
        val list = ArrayList<MultipartBody.Part>()
        flies.filter {
            it.isNotNull()
        }.forEach {
            val file = File(it)
            val responseBody = RequestBody.create(tempMediaType, file)
            list.add(MultipartBody.Part.createFormData(key, file.name, responseBody))
        }
        return list
    }
}