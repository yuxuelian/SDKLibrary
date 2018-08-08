package com.kaibo.mvp.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import com.kaibo.core.fragment.BaseFragment
import com.kaibo.core.toast.ToastUtils
import com.kaibo.core.util.bindToAutoDispose
import com.kaibo.mvp.presenter.BasePresenter
import com.uber.autodispose.AutoDisposeConverter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
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
abstract class AbsMvpFragment<P : BasePresenter> : BaseFragment(), BaseView,
        HasSupportFragmentInjector {

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    abstract var mPresenter: P

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childFragmentInjector
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.onCreate(this)
        lifecycle.addObserver(mPresenter)
    }

    override fun onDestroy() {
        mPresenter.onDestroy(this)
        super.onDestroy()
    }

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

    override fun showLoading() {

    }

    override fun hideLoading() {

    }
}