package com.kaibo.base.mvp

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:37
 * GitHub：
 * email：
 * description：
 */
interface BaseView<out P: BasePresenter<BaseView<P>>> {
    val mPresenter:P

    fun showLoadView()

    fun hideLoadView()

    fun showToast(msg: String)
}