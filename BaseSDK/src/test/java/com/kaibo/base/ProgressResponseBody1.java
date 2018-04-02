package com.kaibo.base;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author Administrator
 * @date 2017/11/30 0030 14:46
 * @description 监听文件下载进度
 */
public class ProgressResponseBody1 extends ResponseBody {

    private ResponseBody responseBody;
    private ProgressListener progressListener;

    private BufferedSource bufferedSource;

    /**
     * 总进度
     */
    private long total;

    public ProgressResponseBody1(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        this.total = responseBody.contentLength();
        return total;
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long progress = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                progress += bytesRead == -1 ? 0 : bytesRead;
                //实时发送当前已读取的字节和总字节
                progressListener.onProgress(progress / (total + 0f));
                return bytesRead;
            }
        };
    }
}
