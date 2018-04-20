package com.kaibo.common.mvp.presenter

import android.support.annotation.CheckResult
import com.kaibo.common.mvp.model.BaseModel
import com.kaibo.common.mvp.view.BaseView
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * @author Administrator
 * @date 2018/3/17 16:33
 * GitHub：
 * email：
 * description：
 * 将生命周期发送到出去   用于实现自动解除对RxJava的订阅
 * 特别说明:子类必须有一个实现了 V  和  M  接口的参数的构造方法
 */
abstract class AbstractRxPresenter<out V : BaseView<*>, out M : BaseModel>(override val mModel: M, override val mView: V) : BasePresenter<V, M>, LifecycleProvider<FragmentEvent> {

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

    override fun onAttach() {
        Logger.d("onAttach")
        lifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    override fun onCreate() {
        Logger.d("onCreate")
        lifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    override fun onViewCreated() {
        Logger.d("onViewCreated")
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
    }

    override fun onStart() {
        Logger.d("onStart")
        lifecycleSubject.onNext(FragmentEvent.START)
    }

    override fun onResume() {
        Logger.d("onResume")
        lifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    override fun onPause() {
        Logger.d("onPause")
        lifecycleSubject.onNext(FragmentEvent.PAUSE)
    }

    override fun onStop() {
        Logger.d("onStop")
        lifecycleSubject.onNext(FragmentEvent.STOP)
    }

    override fun onDestroyView() {
        Logger.d("onDestroyView")
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
    }

    override fun onDestroy() {
        Logger.d("onDestroy")
        lifecycleSubject.onNext(FragmentEvent.DESTROY)
        mView.showToast("执行了  onDestroy")
    }

    override fun onDetach() {
        Logger.d("onDetach")
        lifecycleSubject.onNext(FragmentEvent.DETACH)
    }
}