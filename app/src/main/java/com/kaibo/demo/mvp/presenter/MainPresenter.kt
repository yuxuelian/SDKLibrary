package com.kaibo.demo.mvp.presenter

import com.kaibo.base.mvp.presenter.AbstractRxPresenter
import com.kaibo.demo.mvp.contract.MainContract
import com.kaibo.demo.mvp.model.MainModel
import com.kaibo.demo.mvp.view.MainFragment

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:34
 * GitHub：
 * email：
 * description：
 */

class MainPresenter(mModel: MainModel, mView: MainFragment) : AbstractRxPresenter<MainContract.View, MainContract.Model>(mModel, mView), MainContract.Presenter {

    override fun queryOrderById(id: Long) {
//        mView.showToast("查询到订单  id是 $id")
        mView.showToast(mModel.getTestStr())

    }

}