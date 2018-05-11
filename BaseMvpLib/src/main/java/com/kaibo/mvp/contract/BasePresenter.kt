package com.kaibo.mvp.contract

import android.os.Bundle

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:38
 * GitHub：
 * email：
 * description：
 */
interface BasePresenter<out V : BaseView, out M : BaseModel> {

    val mView: V
    val mModel: M

    fun onCreate(savedInstanceState: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}