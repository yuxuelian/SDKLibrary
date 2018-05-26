package com.kaibo.mvp.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.kaibo.core.fragment.base.BaseFragment
import com.kaibo.mvp.contract.IBaseView
import com.kaibo.mvp.model.BaseBaseModel
import com.kaibo.mvp.presenter.BaseFragmentPresenter
import com.kaibo.core.toast.ToastUtils
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Administrator
 * @date 2018/5/11 0011 下午 2:01
 * @GitHub：https://github.com/yuxuelian
 * @email：
 * @description：
 * Fragment中使用    MVP
 */

abstract class BaseMvpFragment<out P : BaseFragmentPresenter<*, *>, out M : BaseBaseModel> :
        BaseFragment(),
        IBaseView {

    protected val mPresenter: P

    init {
        val actualTypeArguments: Array<Type> = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments

        //创建 presenter 实例
        @Suppress("UNCHECKED_CAST")
        mPresenter = (actualTypeArguments[0] as Class<P>).newInstance()

        //创建 model 实例
        @Suppress("UNCHECKED_CAST")
        val model: M = (actualTypeArguments[1] as Class<M>).newInstance()

        mPresenter.setMV(model, this)
    }


    override fun showLoad() {
    }

    override fun hideLoad() {
    }

    override fun showInfo(msg: String) {
        ToastUtils.showInfo(msg)
    }

    override fun showSuccess(msg: String) {
        ToastUtils.showSuccess(msg)
    }

    override fun showWarning(msg: String) {
        ToastUtils.showWarning(msg)
    }

    override fun showError(msg: String) {
        ToastUtils.showError(msg)
    }

    //绑定生命周期
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mPresenter.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        mPresenter.onPause()
        super.onPause()
    }

    override fun onStop() {
        mPresenter.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        mPresenter.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        mPresenter.onDetach()
        super.onDetach()
    }

}