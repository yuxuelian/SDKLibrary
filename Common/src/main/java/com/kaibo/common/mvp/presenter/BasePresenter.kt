package com.kaibo.common.mvp.presenter

import com.kaibo.common.mvp.model.BaseModel
import com.kaibo.common.mvp.view.BaseView

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:38
 * GitHub：
 * email：
 * description：
 */
interface BasePresenter<out V : BaseView<*>, out M : BaseModel> {

    val mView: V
    val mModel: M

    fun onAttach()
    fun onCreate()
    fun onViewCreated()
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroyView()
    fun onDestroy()
    fun onDetach()
}