package com.kaibo.demo.mvp.contract

import com.kaibo.mvp.contract.IBaseModel
import com.kaibo.mvp.contract.IBasePresenter
import com.kaibo.mvp.contract.IBaseView

/**
 * @author:Administrator
 * @date:2018/3/19 0019 上午 11:00
 * GitHub:
 * email:
 * description:
 */
interface MainContract {

    interface IView : IBaseView {

    }

    interface IPresenter : IBasePresenter<IView, IModel> {
        fun queryOrderById(id: Long)
    }

    interface IModel : IBaseModel {
        fun getTestStr(): String
    }
}