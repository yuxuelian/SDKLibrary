package com.kaibo.mvp.contract

import android.os.Bundle

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:38
 * GitHub：
 * email：
 * description：
 */
interface IBasePresenter<out V : IBaseView, out M : IBaseModel> {

    val mView: V
    val mModel: M

    fun onCreate(savedInstanceState: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}