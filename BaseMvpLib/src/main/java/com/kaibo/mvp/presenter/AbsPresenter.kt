package com.kaibo.mvp.presenter

import android.arch.lifecycle.LifecycleOwner
import com.kaibo.core.util.bindToAutoDispose
import com.kaibo.mvp.view.BaseView
import com.uber.autodispose.AutoDisposeConverter

/**
 * @author:Administrator
 * @date:2018/6/5 0005下午 2:46
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

abstract class AbsPresenter<V : BaseView>(protected val mView: V) : BasePresenter {

    private lateinit var lifecycleOwner: LifecycleOwner

    override fun onCreate(owner: LifecycleOwner) {
        this.lifecycleOwner = owner
    }

    override fun onDestroy(owner: LifecycleOwner) {

    }

    protected fun <T> bindLifecycle(): AutoDisposeConverter<T> = bindToAutoDispose(lifecycleOwner)

}