package com.kaibo.demo.activity

import com.kaibo.base.activity.BaseMvpActivity
import com.kaibo.demo.mvp.model.MainModel
import com.kaibo.demo.mvp.presenter.MainPresenter
import com.kaibo.demo.mvp.view.MainFragment

class MainActivity : BaseMvpActivity<MainFragment, MainPresenter>() {

//    override val mView by lazy {
//        MainFragment.newInstance()
//    }

//    override val mPresenter by lazy {
//        MainPresenter(MainModel())
//    }


    override val mView = MainFragment.newInstance()
    override val mPresenter = MainPresenter(MainModel())

}
