package com.kaibo.demo.contract

import com.kaibo.base.mvp.BasePresenter
import com.kaibo.base.mvp.BaseView


/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 12:58
 * GitHub：
 * email：
 * description：
 */
interface MainContract {

    interface MainView : BaseView {

    }

    interface MainPresenter : BasePresenter<MainView> {
        fun queryOrderById(id: Long)
    }

    interface MainModel {

    }
}