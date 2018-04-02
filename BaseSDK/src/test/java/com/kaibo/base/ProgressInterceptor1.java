package com.kaibo.base;

import com.kaibo.base.http.progress.ProgressResponseBody;

import java.io.IOException;

import kotlin.Unit;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by air on 2016/12/5.
 */
public class ProgressInterceptor1 implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), (aDouble, aBoolean) -> {
                    System.out.println(aDouble);
                    System.out.println(aBoolean);
                    return Unit.INSTANCE;
                }))
                .build();
    }
}
