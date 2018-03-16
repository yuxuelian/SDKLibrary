package com.kaibo.demo.activity

import android.os.Bundle
import com.kaibo.base.activity.BaseMvpViewActivity
import com.kaibo.demo.R
import com.kaibo.demo.contract.MainContract
import com.kaibo.demo.presenter.MainPresenterImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvpViewActivity<MainContract.MainView, MainContract.MainPresenter>(), MainContract.MainView {

    override fun getLayoutResId() = R.layout.activity_main

    /**
     * 创建Presenter
     */
    override fun createPresenter() = MainPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.queryOrderBtn.setOnClickListener {
            mPresenter.queryOrderById(123)
        }
    }
}
