package com.kaibo.mvp.contract

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:37
 * GitHub：
 * email：
 * description：
 */
interface BaseView {

    fun showLoadView()

    fun hideLoadView()

    fun showToast(msg: String)
}