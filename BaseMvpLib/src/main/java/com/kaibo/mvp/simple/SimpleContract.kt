package com.kaibo.mvp.simple

import com.kaibo.mvp.contract.IBaseModel
import com.kaibo.mvp.contract.IBasePresenter
import com.kaibo.mvp.contract.IBaseView

/**
 * @author Administrator
 * @date 2018/5/11 0011 下午 4:31
 * @GitHub：https://github.com/yuxuelian
 * @email：
 * @description：
 */

interface SimpleContract {
    interface IModel : IBaseModel

    interface IView : IBaseView

    interface IPresenter : IBasePresenter<IView, IModel>
}