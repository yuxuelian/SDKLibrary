package com.kaibo.core.http.progress

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.util.concurrent.ConcurrentHashMap

/**
 * @author:Administrator
 * @date:2018/6/1 0001上午 9:06
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

object ProgressObservable {

    val responseProgress: MutableMap<String, ObservableEmitter<ProgressMessage>> by lazy {
        ConcurrentHashMap<String, ObservableEmitter<ProgressMessage>>()
    }

    val requestProgress: MutableMap<String, ObservableEmitter<ProgressMessage>> by lazy {
        ConcurrentHashMap<String, ObservableEmitter<ProgressMessage>>()
    }

    fun listenerResponse(key: String): Observable<ProgressMessage> {
        return Observable
                .create<ProgressMessage> {
                    responseProgress[key] = it
                }
                .doOnComplete {
                    responseProgress.remove(key)
                }
                .doOnError {
                    responseProgress.remove(key)
                }
//                .toMainThread()
    }

    fun listenerRequest(key: String): Observable<ProgressMessage> {
        return Observable
                .create<ProgressMessage> {
                    requestProgress[key] = it
                }
                .doOnComplete {
                    requestProgress.remove(key)
                }
                .doOnError {
                    requestProgress.remove(key)
                }
//                .toMainThread()
    }

}

