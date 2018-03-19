package com.kaibo.base.activity

import android.os.Bundle
import com.kaibo.base.R
import com.kaibo.base.mvp.presenter.AbstractPresenter
import com.kaibo.base.mvp.view.AbstractFragment
import com.kaibo.base.util.ActivityUtils

/**
 * @author Administrator
 * @date 2018/3/19 0019 上午 11:32
 * GitHub：
 * email：
 * description：使用MVP的Activity继承
 */


abstract class BaseMvpActivity<out V : AbstractFragment<*>, out P : AbstractPresenter<*>> : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMvp()
    }

    override fun getLayoutRes() = R.layout.activity_mvp_layout

    /**
     * 创建V和P
     */
    abstract val mView: V
    abstract val mPresenter: P

    /**
     * 初始化MVP
     */
    private fun initMvp() {
        supportFragmentManager.findFragmentById(R.id.mvpViewContainer)
                ?: {
                    ActivityUtils.addFragmentToActivity(supportFragmentManager, mView, R.id.mvpViewContainer)
                    mView
                }.invoke().setPresenter(mPresenter)
    }
}