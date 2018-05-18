package com.kaibo.mvp.contract

import android.content.Context
import android.os.Bundle
import android.view.View

/**
 * @author Administrator
 * @date 2018/5/11 0011 下午 2:08
 * @GitHub：https://github.com/yuxuelian
 * @email：
 * @description：
 */

interface IBaseFragmentPresenter<out V : IBaseView, out M : IBaseModel> : IBasePresenter<V, M> {
    fun onAttach(context: Context?)
    fun onViewCreated(view: View, savedInstanceState: Bundle?)
    fun onDestroyView()
    fun onDetach()
}
