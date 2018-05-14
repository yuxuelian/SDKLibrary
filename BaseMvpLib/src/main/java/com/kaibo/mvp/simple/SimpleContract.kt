package com.kaibo.mvp.simple

import com.kaibo.mvp.contract.BaseModel
import com.kaibo.mvp.contract.BasePresenter
import com.kaibo.mvp.contract.BaseView

/**
 * @author Administrator
 * @date 2018/5/11 0011 下午 4:31
 * @GitHub：https://github.com/yuxuelian
 * @email：
 * @description：
 */

interface SimpleContract {
    interface Model : BaseModel

    interface View : BaseView

    interface Presenter : BasePresenter<View, Model>
}