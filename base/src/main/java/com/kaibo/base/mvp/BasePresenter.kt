package com.kaibo.base.mvp

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:38
 * GitHub：
 * email：
 * description：
 */
interface BasePresenter<in T : BaseView> {

    fun attachView(view: T)

    fun detachView()
}