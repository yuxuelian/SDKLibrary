package com.kaibo.base.activity

import android.os.Bundle
import com.kaibo.base.mvp.BasePresenter
import com.kaibo.base.mvp.BaseView
import com.kaibo.base.util.ToastUtil

/**
 * @author Administrator
 * @date 2018/3/16 0016 上午 11:36
 * GitHub：
 * email：
 * description：
 */
abstract class BaseMvpViewActivity<in V : BaseView, out T : BasePresenter<V>> : BaseActivity(), BaseView {

    protected val mPresenter = this.createPresenter()

    abstract fun createPresenter(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this as V)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    override fun showLoadView() {

    }

    override fun hideLoadView() {

    }

    override fun showToast(msg: String) {
        ToastUtil.showToast(msg)
    }
}