package com.kaibo.mvp.contract

/**
 * @author Administrator
 * @date 2018/5/11 0011 下午 2:08
 * @GitHub：https://github.com/yuxuelian
 * @email：
 * @description：
 */

interface BaseFragmentPresenter<out V : BaseView, out M : BaseModel> : BasePresenter<V, M> {
    fun onAttach()
    fun onViewCreated()
    fun onDestroyView()
    fun onDetach()
}
