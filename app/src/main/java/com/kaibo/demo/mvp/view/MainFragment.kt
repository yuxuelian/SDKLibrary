package com.kaibo.demo.mvp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import com.kaibo.common.mvp.view.AbstractFragment
import com.kaibo.common.util.sha1
import com.kaibo.common.util.immersiveTopView
import com.kaibo.common.util.toAppSetting
import com.kaibo.demo.R
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
        immersiveTopView(appBarLayout)
    }

    override fun getLayoutRes() = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            //            mPresenter.queryOrderById(123)
//            mAttachActivity.startActivity<SwipeBackActivity>()

            context?.toAppSetting()

            Log.d("TAG", context?.sha1)
        }

    }
}