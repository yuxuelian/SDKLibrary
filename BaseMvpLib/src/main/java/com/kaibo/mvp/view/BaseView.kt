package com.kaibo.mvp.view

import android.support.annotation.MainThread

/**
 * @author:Administrator
 * @date:2018/6/5 0005上午 11:08
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

interface BaseView {

    @MainThread
    fun showSuccess(success: String?)

    @MainThread
    fun showError(error: String?)

    @MainThread
    fun showInfo(info: String?)

    @MainThread
    fun showWarning(warning: String?)

    @MainThread
    fun showLoading()

    @MainThread
    fun hideLoading()

}