package com.kaibo.core.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * @author:Administrator
 * @date:2018/3/16 0016 上午 11:41
 * GitHub:
 * email:
 * description:
 * 非  MVP 的 Activity  继承
 */

abstract class BaseRxLifecycleActivity : BaseActivity(), LifecycleProvider<ActivityEvent> {

    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()


    override fun setContentViewBefore(savedInstanceState: Bundle?) {
        lifecycleSubject.onNext(ActivityEvent.CREATE)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        super.onDestroy()
    }

    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }
}
