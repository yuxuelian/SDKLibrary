package com.kaibo.base.http.progress

import com.kaibo.base.http.HttpRequestManager
import com.kaibo.base.util.leaveTwoDecimal
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

    private val requestBody: RequestBody

    private val progressListener: (progress: Double, isFinish: Boolean) -> Unit

    private var bufferedSink: BufferedSink? = null

    /**
     * 总长度
     */
    private var total: Long = 0

    constructor(file: File, progressListener: (progress: Double, isFinish: Boolean) -> Unit) {
        this.progressListener = progressListener
        this.requestBody = RequestBody.create(HttpRequestManager.FORM_DATA, file)
    }

    constructor(requestBody: RequestBody, progressListener: (progress: Double, isFinish: Boolean) -> Unit) {
        this.progressListener = progressListener
        this.requestBody = requestBody
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
        this.total = requestBody.contentLength()
        return total
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
            internal var currentTotal = 0L

            //记录上一次的进度值
            internal var lastProgress = 0.0

            //记录是否已经完成
            internal var isFinish = false

            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                //增加当前写入的字节数
                currentTotal += if (byteCount < 0L) 0L else byteCount

                val currentProgress = (currentTotal / total.toDouble()).leaveTwoDecimal()

                //过滤相同的进度值
                if (currentTotal == total && !isFinish) {
                    isFinish = true
                    //下载完成了
                    progressListener(currentProgress, true)
                } else {
                    if (currentProgress > lastProgress) {
                        //回调lambda表达式
                        progressListener(currentProgress, false)
                        lastProgress = currentProgress
                    }
                }
            }
        }
    }
}