package com.kaibo.demo.mvp.view

import android.os.Bundle
import android.view.View
import com.kaibo.base.mvp.view.AbstractFragment
import com.kaibo.base.util.immersiveTitleView
import com.kaibo.base.util.startActivity
import com.kaibo.demo.R
import com.kaibo.demo.activity.SwipeBackActivity
import com.kaibo.demo.mvp.contract.MainContract
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.include_title.*

/**
 * @author Administrator
 * @date 2018/3/19 0019 上午 11:00
 * GitHub：
 * email：
 * description：
 */

class MainFragment : AbstractFragment<MainContract.Presenter>(), MainContract.View {

    override fun initViewCreated(savedInstanceState: Bundle?) {
        immersiveTitleView(appBarLayout)
    }

    override fun getLayoutRes() = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            //            mPresenter.queryOrderById(123)
            mAttachActivity.startActivity<SwipeBackActivity>()
        }
    }
}