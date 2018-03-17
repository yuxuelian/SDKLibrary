package com.kaibo.demo.activity

import android.os.Bundle
import com.kaibo.base.activity.BaseMvpActivity
import com.kaibo.base.util.ToastUtil
import com.kaibo.demo.R
import com.kaibo.demo.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvpActivity<MainPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.queryOrderBtn.setOnClickListener {
            mPresenter.queryOrderById(123)
        }
    }

    fun showToast2() {
        ToastUtil.showToast("123")
    }
}
