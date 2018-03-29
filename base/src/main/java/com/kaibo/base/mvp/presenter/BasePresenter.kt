package com.kaibo.base.mvp.presenter

import com.kaibo.base.mvp.view.BaseView

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:38
 * GitHub：
 * email：
 * description：
 */
interface BasePresenter<out V : BaseView<BasePresenter<V>>> {

    fun onResume()

    fun onDestroy()

}