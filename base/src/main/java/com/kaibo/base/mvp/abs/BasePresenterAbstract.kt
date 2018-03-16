package com.kaibo.base.mvp.abs

import com.kaibo.base.mvp.BasePresenter
import com.kaibo.base.mvp.BaseView


/**
 * @author Administrator
 * @date 2018/3/16 0016 上午 11:42
 * GitHub：
 * email：
 * description：
 */
abstract class BasePresenterAbstract<T : BaseView> : BasePresenter<T> {

    protected var mView: T? = null

    override fun attachView(view: T) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }
}