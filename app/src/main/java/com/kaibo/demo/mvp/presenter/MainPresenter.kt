package com.kaibo.demo.mvp.presenter

import com.kaibo.demo.mvp.contract.MainContract
import com.kaibo.mvp.presenter.AbstractPresenter

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:34
 * GitHub：
 * email：
 * description：
 */

class MainPresenter : AbstractPresenter<MainContract.View, MainContract.Model>(),
        MainContract.Presenter {

    override fun queryOrderById(id: Long) {
//        mView.showInfo("查询到订单  id是 $id")
        mView.showInfo(mModel.getTestStr())

    }

}