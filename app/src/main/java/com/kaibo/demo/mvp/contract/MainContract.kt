package com.kaibo.demo.mvp.contract

import com.kaibo.base.mvp.model.BaseModel
import com.kaibo.base.mvp.presenter.BasePresenter
import com.kaibo.base.mvp.view.BaseView

/**
 * @author Administrator
 * @date 2018/3/19 0019 上午 11:00
 * GitHub：
 * email：
 * description：
 */
interface MainContract {

    interface View : BaseView<Presenter>

    interface Presenter : BasePresenter<View> {
        fun queryOrderById(id: Long)
    }

    interface Model : BaseModel{

    }
}