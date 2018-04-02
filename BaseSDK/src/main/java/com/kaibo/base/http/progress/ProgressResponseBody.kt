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

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return response.contentType()
    }

    override fun contentLength(): Long {
        this.total = response.contentLength()
        return total
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(response.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            //当前读取的字节数
            private var currentTotal: Long = 0L

            //记录上一次的进度值
            private var lastProgress = 0.0

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)

                if (bytesRead == -1L) {
                    //读取结束
                    progressListener(1.0, true)
                } else {
                    currentTotal += bytesRead
                    val currentProgress = (currentTotal / total.toDouble()).leaveTwoDecimal()
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