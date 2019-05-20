package com.kaibo.core.util

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author kaibo
 * @date 2019/2/18 10:53
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

fun launchUI(start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit): Job =
        GlobalScope.launch(Dispatchers.Main, start, block)

fun Job.asAutoDisposable(view: View) = AutoDisposableJob(view, this)

fun View.onClickAutoDisposable(context: CoroutineContext = Dispatchers.Main, handler: suspend CoroutineScope.(v: View?) -> Unit) {
    setOnClickListener { v ->
        GlobalScope.launch(context, CoroutineStart.DEFAULT) {
            handler(v)
        }.asAutoDisposable(v)
    }
}

fun Fragment.launchUI(untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
                      start: CoroutineStart = CoroutineStart.DEFAULT,
                      block: suspend CoroutineScope.() -> Unit): Job {
    val job = GlobalScope.launch(Dispatchers.Main, start, block)
    lifecycle.addObserver(
            @SuppressLint("RestrictedApi")
            object : GenericLifecycleObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == untilEvent) {
                        job.cancel()
                        lifecycle.removeObserver(this)
                    }
                }
            }
    )
    return job
}

fun ComponentActivity.launchUI(untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
                               start: CoroutineStart = CoroutineStart.DEFAULT,
                               block: suspend CoroutineScope.() -> Unit): Job {
    val job = GlobalScope.launch(Dispatchers.Main, start, block)
    lifecycle.addObserver(
            @SuppressLint("RestrictedApi")
            object : GenericLifecycleObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == untilEvent) {
                        job.cancel()
                        lifecycle.removeObserver(this)
                    }
                }
            }
    )
    return job
}

class AutoDisposableJob(private val view: View, private val wrapped: Job) : Job by wrapped, View.OnAttachStateChangeListener {

    override fun onViewAttachedToWindow(v: View?) = Unit

    override fun onViewDetachedFromWindow(v: View?) {
        cancel()
        view.removeOnAttachStateChangeListener(this)
    }

    private fun isViewAttached() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && view.isAttachedToWindow || view.windowToken != null

    init {
        if (isViewAttached()) {
            view.addOnAttachStateChangeListener(this)
        } else {
            cancel()
        }
        invokeOnCompletion {
            view.removeOnAttachStateChangeListener(this)
        }
    }
}
