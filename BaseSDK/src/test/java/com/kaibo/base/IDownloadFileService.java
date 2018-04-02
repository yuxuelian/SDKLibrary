package com.kaibo.base;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author Administrator
 * @date 2017/11/30 0030 14:18
 * @description
 */
public interface IDownloadFileService {

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String loadUrl);
}
