package com.kaibo.demo.presenter

import com.kaibo.base.mvp.abs.BasePresenterAbstract
import com.kaibo.demo.contract.MainContract
import com.kaibo.demo.model.MainModelImpl

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:34
 * GitHub：
 * email：
 * description：
 */

class MainPresenterImpl : BasePresenterAbstract<MainContract.MainView>(), MainContract.MainPresenter {

    private val model: MainContract.MainModel = MainModelImpl()


    override fun queryOrderById(id: Long) {
        mView?.showToast("查询到了订单  id是 $id")
    }

}