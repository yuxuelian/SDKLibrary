package com.kaibo.base.http.progress

import com.kaibo.base.util.leaveTwoDecimal
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*


/**
 * @author Administrator
 * @date 2018/4/2 0002 下午 1:54
 * GitHub：
 * email：
 * description：下载文件下载进度监听
 */
internal class ProgressResponseBody(private val response: ResponseBody, private val progressListener: (progress: Double, isFinish: Boolean) -> Unit) : ResponseBody() {

    /**
     * 总进度
     */
    private var total: Long = 0

    private var bufferedSource: BufferedSource? = Okio.buffer(source(response.source()))

    override fun contentType(): MediaType? {
        return response.contentType()
    }

    override fun contentLength(): Long {
        this.total = response.contentLength()
        return total
    }

    override fun source(): BufferedSource? {
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            //当前读取的字节数
            internal var currentTotal: Long = 0L

            //记录上一次的进度值
            internal var lastProgress = 0.0

            //记录是否已经完成
            internal var isFinish = false

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                currentTotal += if (bytesRead < 0L) 0L else bytesRead

                val currentProgress = (currentTotal / total.toDouble()).leaveTwoDecimal()

                if (currentTotal == total && !isFinish) {
                    isFinish = true
                    //下载完成了
                    progressListener(currentProgress, true)
                } else {
                    //过滤相同的进度值
                    if (currentProgress > lastProgress) {
                        //回调lambda表达式
                        progressListener(currentProgress, false)
                        lastProgress = currentProgress
                    }
                }

                return bytesRead
            }
        }
    }
}