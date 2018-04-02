package com.kaibo.base

import com.kaibo.base.http.HttpRequestManager
import com.kaibo.base.http.progress.ProgressInterceptor
import okhttp3.OkHttpClient
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.io.FileOutputStream

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }


    @Test
    fun test() {

        val file = File("""H:\\qq.apk""")

        if (file.exists()) {
            file.createNewFile()
        }

        val outputStream = FileOutputStream(file)

        HttpRequestManager
                .retrofit
                .create(TestApi::class.java)
                .downLoadFile("https://qd.myapp.com/myapp/qqteam/Androidlite/qqlite_3.6.3.697_android_r110028_GuanWang_537055374_release_10000484.apk")
                .subscribe({
                    val buff = ByteArray(2048)
                    val byteStream = it.byteStream()
                    while (byteStream.read(buff) != -1) {
                        outputStream.write(buff)
                    }
                    outputStream.flush()
                    byteStream.close()
                    outputStream.close()
                })

        while (true) {

        }
    }

    @Test
    fun downloadFile() {
        val file = File("""H:\\qq.apk""")

        if (file.exists()) {
            file.createNewFile()
        }

        val outputStream = FileOutputStream(file)

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ProgressInterceptor())
                .build()

        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://admin.eyunhe.com.cn/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        retrofit
                .create(IDownloadFileService::class.java)
                .downloadFile("http://admin.eyunhe.com.cn:80/upload/software/app-release.apk")
                .subscribe { it ->
                    val buff = ByteArray(2048)
                    val byteStream = it.byteStream()
                    while (byteStream.read(buff) != -1) {
                        outputStream.write(buff)
                    }
                    outputStream.flush()
                    byteStream.close()
                    outputStream.close()
                }
    }
}