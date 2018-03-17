package com.kaibo.demo.activity

import android.os.Bundle
import com.kaibo.base.activity.BaseMvpActivity
import com.kaibo.demo.R
import com.kaibo.demo.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvpActivity<MainPresenter>() {

    override fun getLayoutResId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.queryOrderBtn.setOnClickListener {
            mPresenter.queryOrderById(123)
        }
    }
}
