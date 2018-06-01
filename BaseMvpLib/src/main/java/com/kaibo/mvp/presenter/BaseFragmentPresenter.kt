package com.kaibo.mvp.presenter

import android.content.Context
import android.os.Bundle
import android.support.annotation.CheckResult
import android.view.View
import com.kaibo.mvp.contract.IBaseFragmentPresenter
import com.kaibo.mvp.contract.IBaseModel
import com.kaibo.mvp.contract.IBaseView
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
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
abstract class BaseFragmentPresenter<out V : IBaseView, out M : IBaseModel> :
        IBaseFragmentPresenter<V, M>,
        LifecycleProvider<FragmentEvent> {

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

    private val lifecycleSubject = BehaviorSubject.create<FragmentEvent>()

    @CheckResult
    override fun lifecycle(): Observable<FragmentEvent> {
        return lifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject)
    }

    override fun onAttach(context: Context?) {
        lifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
    }

    override fun onStart() {
        lifecycleSubject.onNext(FragmentEvent.START)
    }

    override fun onResume() {
        lifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    override fun onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE)
    }

    override fun onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP)
    }

    override fun onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
    }

    override fun onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY)
    }

    override fun onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH)
    }
}