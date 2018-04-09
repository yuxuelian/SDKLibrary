package com.kaibo.base.mvp.view

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.kaibo.base.fragment.base.BaseFragment
import com.kaibo.base.mvp.presenter.BasePresenter
import com.kaibo.base.util.ToastUtils


/**
 * @author Administrator
 * @date 2018/3/19 0019 上午 10:55
 * GitHub：
 * email：
 * description：将Fragment的生命周期传递到Presenter中去
 */
abstract class AbstractFragment<out P : BasePresenter<BaseView<P>, *>> : BaseFragment(), BaseView<P> {

    /**
     * 当Fragment实例创建后,请立即对  Presenter  进行赋值
     */
    override lateinit var mPresenter: @UnsafeVariance P

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mPresenter.onAttach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.onViewCreated()
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        mPresenter.onPause()
        super.onPause()
    }

    override fun onStop() {
        mPresenter.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        mPresenter.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        mPresenter.onDetach()
        super.onDetach()
    }

    override fun showLoadView() {

    }

    override fun hideLoadView() {

    }

    override fun showToast(msg: String) {
        ToastUtils.showToast(msg)
    }
}