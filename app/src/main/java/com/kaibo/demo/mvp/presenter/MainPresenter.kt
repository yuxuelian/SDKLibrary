package com.kaibo.demo.mvp.presenter

import com.kaibo.base.mvp.presenter.AbstractPresenter
import com.kaibo.demo.mvp.contract.MainContract

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:34
 * GitHub：
 * email：
 * description：
 */

class MainPresenter(private val mModel: MainContract.Model) : AbstractPresenter<MainContract.View>(), MainContract.Presenter {

    override fun queryOrderById(id: Long) {
        mView.showToast("查询到订单  id是 $id")
    }

}