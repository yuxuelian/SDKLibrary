package com.kaibo.demo.mvp.contract

import com.kaibo.mvp.contract.BaseModel
import com.kaibo.mvp.contract.BasePresenter
import com.kaibo.mvp.contract.BaseView

/**
 * @author Administrator
 * @date 2018/3/19 0019 上午 11:00
 * GitHub：
 * email：
 * description：
 */
interface MainContract {

    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View, Model> {
        fun queryOrderById(id: Long)
    }

    interface Model : BaseModel {
        fun getTestStr(): String
    }
}