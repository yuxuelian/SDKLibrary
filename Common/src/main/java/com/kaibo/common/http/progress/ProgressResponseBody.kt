package com.kaibo.common.http.progress

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
class ProgressResponseBody(private val url: String, private val response: ResponseBody) : ResponseBody() {

    private val progressListener =  ProgressListener.downloadProgressListeners[url]

    /**
     * 总进度
     */
    private var fillLength = 0L

    private var step = 0L

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return response.contentType()
    }

    override fun contentLength(): Long {
        this.fillLength = response.contentLength()
        this.step = fillLength / 100L
        println("步长  $step")
        return fillLength
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
            private var currentLength = 0L

            //上次进度
            private var lastLength = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)

                if (bytesRead == -1L) {
                    //读取结束
                    progressListener?.invoke(fillLength, fillLength, true)
                    //移除
                    ProgressListener.downloadProgressListeners.remove(url)
                } else {
                    currentLength += bytesRead
                    if (currentLength - lastLength >= step) {
                        progressListener?.invoke(currentLength, fillLength, false)
                        //更新记录
                        lastLength = currentLength
                    }
                }
                return bytesRead
            }
        }
    }
}