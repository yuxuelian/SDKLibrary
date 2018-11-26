package com.kaibo.mvp.view

import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import com.kaibo.core.activity.SuperActivity
import com.kaibo.core.toast.ToastUtils
import com.kaibo.mvp.presenter.BasePresenter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import dagger.internal.Beta
import javax.inject.Inject

/**
 * @author:Administrator
 * @date:2018/6/5 0005下午 2:27
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

@Beta
abstract class AbsMvpActivity<P : BasePresenter> : SuperActivity(), BaseView,
        HasFragmentInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    abstract var mPresenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        //初始化Inject注入
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setContentViewBefore(savedInstanceState: Bundle?) {
        //绑定生命周期
        mPresenter.onCreate(this)
        //将 mPresenter  与 Activity的生命周期绑定起来
        lifecycle.addObserver(mPresenter)
    }

    override fun onDestroy() {
        mPresenter.onDestroy(this)
        super.onDestroy()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return supportFragmentInjector
    }

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment> {
        return frameworkFragmentInjector
    }

    @MainThread
    override fun showSuccess(success: String?) {
        ToastUtils.showSuccess(success)
    }

    override fun showError(error: String?) {
        ToastUtils.showError(error)
    }

    override fun showInfo(info: String?) {
        ToastUtils.showInfo(info)
    }

    override fun showWarning(warning: String?) {
        ToastUtils.showWarning(warning)
    }
}