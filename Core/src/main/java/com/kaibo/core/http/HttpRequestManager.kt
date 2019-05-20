package com.kaibo.core.http

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.serializationConverterFactory
import com.kaibo.core.AppContext
import com.kaibo.core.BuildConfig
import com.kaibo.core.http.interceptor.ProgressInterceptor
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author:Administrator
 * @date:2018/3/28 0028 上午 11:21
 * GitHub:
 * email:
 * description:
 */

object HttpRequestManager {

    const val BASE_URL = "http://118.89.19.78:7000/"

    //缓存大小   20M
    private const val CACHE_SIZE = 1024 * 1024 * 20L

    //连接超时时间  30L
    private const val CONNECT_TIMEOUT_TIME = 20L
    //读超时时间  30L
    private const val READ_TIMEOUT_TIME = 20L
    //写超时时间  30L
    private const val WRITE_TIMEOUT_TIME = 20L

    //上传图片
    val IMAGE_MEDIA_TYPE: MediaType = MediaType.get("image/*")
    //上传json
    val mediaTypeJson: MediaType = MediaType.get("application/json; charset=utf-8")
    //普通文本
    val TEXT: MediaType = MediaType.get("text/plain; charset=utf-8")
    //文件
    val FORM_DATA: MediaType = MediaType.get("multipart/form-data")

    private val interceptors: MutableList<Interceptor> = ArrayList()

    /**
     * 此方法需要在  okHttpClient  第一次被使用之前调用   否则无效
     */
    fun setOtherInterceptor(vararg interceptors: Interceptor) {
        HttpRequestManager.interceptors.addAll(interceptors)
    }

    /**
     * 全局唯一一个 OkHttpClient  实例
     */
    private val okHttpClient: OkHttpClient by lazy {
        val builder: OkHttpClient.Builder = OkHttpClient
                .Builder()
                .addInterceptor {
                    it.proceed(it.request()
                            .newBuilder()
                            .addHeader("Connection", "close")
                            .build())
                }
                //失败重连
                .retryOnConnectionFailure(false)
                .connectTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_TIME, TimeUnit.SECONDS)
                .cache(Cache(File("${AppContext.cacheDir.absolutePath}${File.separator}okHttpCaches"), CACHE_SIZE))

        // 根据编译环境设置不同的拦截器
        if (BuildConfig.DEBUG) {
            // 请求日志拦截器
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        } else {
            //进度拦截器
            builder.addInterceptor(ProgressInterceptor())
        }

        //添加别的拦截器
        interceptors.forEach {
            builder.addInterceptor(it)
        }

        builder.build()
    }

    /**
     * Retrofit   实例
     */
    val retrofit: Retrofit by lazy {
        Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
                // serialization
                .addConverterFactory(serializationConverterFactory(mediaTypeJson, Json.nonstrict))
                //同步发出请求
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //使用okhttp的线程池
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                //指定在RxJava的线程池发出请求
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                // 协程方式
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
    }
}