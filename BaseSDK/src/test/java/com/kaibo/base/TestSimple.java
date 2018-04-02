package com.kaibo.base;


import android.util.Log;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 项目名称：ECanal
 * 公司：山东宇易信息科技
 *
 * @author Administrator
 * @date 2017/9/26 0026 11:04
 * 备注：
 */

public class TestSimple {


    /**
     * 单文件上传测试
     */
    @Test
    public void uploadFile() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://admin.eyunhe.com.cn/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        File file = new File("C:\\Users\\Administrator\\Desktop\\test.png");

        //带上传进度上传
        ProgressRequestBody requestBody = new ProgressRequestBody(file, new ProgressListener() {
            @Override
            public void onProgress(float progress) {
                System.out.println("progress=" + progress);
            }
        });

//        ProgressRequestBody requestBody = new ProgressRequestBody(RequestBody.create(MediaType.parse("multipart/form-data"), file), new ProgressListener() {
//            @Override
//            public void onProgress(long bytesWritten, long contentLength, boolean b) {
//                double progress = (double) (bytesWritten / contentLength);
//                System.out.println(progress);
//            }
//        });

        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

//        retrofit
//                .create(IDownloadFileService.class)
//                .uploadFile(part)
//                .subscribe(new Consumer<ResponseBody>() {
//                    @Override
//                    public void accept(ResponseBody responseBody) throws Exception {
//                        String res = responseBody.string();
//                        System.out.println(res);
//                    }
//                });
    }

    /**
     * 多文件上传测试
     */
    @Test
    public void uploadFiles() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://admin.eyunhe.com.cn/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        List<MultipartBody.Part> files = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            File file = new File("C:\\Users\\Administrator\\Desktop\\test.png");
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            files.add(part);
        }

//        retrofit
//                .create(IUploadFileService.class)
//                .uploadFiles(files)
//                .subscribe(new Consumer<ResponseBody>() {
//                    @Override
//                    public void accept(ResponseBody responseBody) throws Exception {
//                        String res = responseBody.string();
//                        System.out.println(res);
//                    }
//                });

    }

    @Test
    public void downloadFile() {
        //接收粘性事件
//        RxBus
//                .getInstance()
//                .toObservableSticky(ProgressEvent.class)
//                .subscribe(new Consumer<ProgressEvent>() {
//                    @Override
//                    public void accept(ProgressEvent progressEvent) throws Exception {
//                        System.out.println("progress=" + progressEvent.getProgress());
//                    }
//                });

        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(new ProgressInterceptor1())
                .build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl("http://admin.eyunhe.com.cn/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        retrofit
                .create(IDownloadFileService.class)
                .downloadFile("http://admin.eyunhe.com.cn:80/upload/software/app-release.apk")
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        saveFile(responseBody);
                    }
                });
    }

    /**
     * 保存文件
     *
     * @param body
     */
    private void saveFile(ResponseBody body) {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = body.byteStream();
            File dir = new File("C:\\Users\\Administrator\\Desktop\\");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, "app-release.apk");
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                Log.e("saveFile", e.getMessage());
            }
        }
    }
}
