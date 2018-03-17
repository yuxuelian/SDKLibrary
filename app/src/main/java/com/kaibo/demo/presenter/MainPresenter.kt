package com.kaibo.demo.presenter

import com.kaibo.base.mvp.abs.AbstractMvpPresenter
import com.kaibo.demo.activity.MainActivity

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:34
 * GitHub：
 * email：
 * description：
 */

class MainPresenter : AbstractMvpPresenter<MainActivity>() {

    fun queryOrderById(id: Long) {
        view?.showToast("查询到了订单  id是 $id")
    }

}