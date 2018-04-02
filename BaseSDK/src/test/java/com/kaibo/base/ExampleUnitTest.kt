package com.kaibo.base

import com.kaibo.base.http.HttpRequestManager
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
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
                .downLoadFile()
                .subscribeOn(Schedulers.io())
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

        while (true){

        }
    }
}