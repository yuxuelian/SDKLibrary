package com.kaibo.base;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Meiji
 * @date 2017/4/22
 */

public class RetrofitFactory {

    private Retrofit retrofit;

    private RetrofitFactory()  {
        //   日志拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                String text = "";
                try {
                    text = URLDecoder.decode(message, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Logger.d("OKHttp-----", text);
                }
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File cacheFile = new File("H:\\", "RetrofitCache");
        if (!cacheFile.exists()) {
            try {
                cacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //100Mb
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
//                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://www.baidu.com")
                .build();
    }

    /**
     * 创建单例
     */
    private static class SingletonHolder {
        private static final Retrofit INSTANCE = new RetrofitFactory().retrofit;
    }

    public static Retrofit getDefaultRetrofit() {
        return SingletonHolder.INSTANCE;
    }

//    class HttpCacheInterceptor implements Interceptor {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            if (!NetWorkUtil.isConnected()) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build();
//                Logger.d("Okhttp", "no network");
//            }

//            Response originalResponse = chain.proceed(request);
//            if (NetWorkUtil.isConnected()) {
//                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
//                String cacheControl = request.cacheControl().toString();
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", cacheControl)
//                        .removeHeader("Pragma")
//                        .build();
//            } else {
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
//                        .removeHeader("Pragma")
//                        .build();
//            }
//        }
//    }
}
