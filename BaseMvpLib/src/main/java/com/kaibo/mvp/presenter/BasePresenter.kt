package com.kaibo.mvp.presenter

import android.os.Bundle
import android.support.annotation.CheckResult
import com.kaibo.mvp.contract.IBaseModel
import com.kaibo.mvp.contract.IBasePresenter
import com.kaibo.mvp.contract.IBaseView
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * @author:Administrator
 * @date:2018/3/17 16:33
 * GitHub:
 * email:
 * description:
 * 将生命周期发送到出去   用于实现自动解除对RxJava的订阅
 */
abstract class BasePresenter<out V : IBaseView, out M : IBaseModel> :
        IBasePresenter<V, M>,
        LifecycleProvider<ActivityEvent> {

    override val mModel by lazy {
        _model
    }

    override val mView by lazy {
        _view
    }

    private lateinit var _model: M
    private lateinit var _view: V

    fun setMV(model: @UnsafeVariance M, view: @UnsafeVariance V) {
        this._model = model
        this._view = view
    }

    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleSubject.onNext(ActivityEvent.CREATE)
    }

    override fun onStart() {
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    override fun onResume() {
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    override fun onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
    }

    override fun onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP)
    }

    override fun onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
    }
}