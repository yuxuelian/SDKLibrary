package com.kaibo.common.http.progress

import com.kaibo.common.http.HttpRequestManager
import com.kaibo.common.util.leaveTwoDecimal
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.File


/**
 * @author Administrator
 * @date 2018/4/2 0002 下午 1:42
 * GitHub：
 * email：
 * description：上传文件  上传进度监听
 */
class ProgressRequestBody : RequestBody {

    private val url: String

    private val requestBody: RequestBody

    private val progressListener: ((currentLength: Long, fillLength: Long, done: Boolean) -> Unit)?

    private var bufferedSink: BufferedSink? = null

    /**
     * 总长度
     */
    private var fillLength: Long = 0

    private var step = 0L

    constructor(url: String, file: File) {
        this.url = url
        this.requestBody = RequestBody.create(HttpRequestManager.FORM_DATA, file)

        this.progressListener = ProgressListener.uploadProgressListener[url]
    }

    constructor(url: String, requestBody: RequestBody) {
        this.url = url
        this.requestBody = requestBody

        this.progressListener = ProgressListener.uploadProgressListener[url]
    }

    /**
     * 返回了requestBody的类型，像什么form-data/MP3/MP4/png等等等格式
     */
    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    /**
     * 返回了本RequestBody的长度，也就是上传的totalLength
     */
    override fun contentLength(): Long {
        //获取总长度,保存到全局变量中去
        this.fillLength = requestBody.contentLength()
        step = fillLength / 100L
        return fillLength
    }

    override fun writeTo(sink: BufferedSink) {
        //包装
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink))
        }

        //写入
        requestBody.writeTo(bufferedSink!!)

        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink!!.flush()
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入的总字节数
            private var currentLength = 0L

            //上一次的进度值
            private var lastLength = 0L

            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (byteCount == -1L) {
                    //写入完成
                    progressListener?.invoke(fillLength, fillLength, true)

                    //移除
                    ProgressListener.uploadProgressListener.remove(url)
                } else {
                    currentLength += byteCount
                    //过滤进度
                    if (currentLength - lastLength >= step) {
                        progressListener?.invoke(fillLength, fillLength, false)
                        lastLength = currentLength
                    }
                }
            }
        }
    }
}