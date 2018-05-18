package com.kaibo.common

import com.kaibo.common.http.HttpRequestManager
import com.kaibo.common.http.progress.ProgressListener
import org.junit.Assert.assertEquals
import org.junit.Test
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
        val url = "https://qd.myapp.com/myapp/qqteam/Androidlite/qqlite_3.6.3.697_android_r110028_GuanWang_537055374_release_10000484.apk"
        ProgressListener.downloadProgressListeners[url] = { currentLength, fillLength, done ->
            //            println("currentLength=$currentLength fillLength=$fillLength  done=$done")
            println(Thread.currentThread().name)
        }

        HttpRequestManager
                .retrofit
                .create(TestApi::class.java)
                .downLoadFile(url)
//                .observeOn(AndroidSchedulers.mainThread())
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
    fun test2() {
        println(System.getProperty("java.vm.version"))
//        val str: String? = null
//        println(str.toString())
//        println("123".isNotEmpty())
    }
}