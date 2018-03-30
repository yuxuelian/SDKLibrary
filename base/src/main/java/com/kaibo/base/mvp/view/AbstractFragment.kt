package com.kaibo.base.mvp.view

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kaibo.base.annotation.PoKo
import com.kaibo.base.mvp.presenter.BasePresenter
import com.kaibo.base.util.ToastUtil


/**
 * @author Administrator
 * @date 2018/3/19 0019 上午 10:55
 * GitHub：
 * email：
 * description：
 */
@PoKo
abstract class AbstractFragment<out P : BasePresenter<BaseView<P>, *>> : Fragment(), BaseView<P> {

    override lateinit var mPresenter: @UnsafeVariance P

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = inflater?.inflate(getLayoutRes(), container, false)

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

    override fun showLoadView() {

    }

    override fun hideLoadView() {

    }

    override fun showToast(msg: String) {
        ToastUtil.showToast(msg)
    }
}