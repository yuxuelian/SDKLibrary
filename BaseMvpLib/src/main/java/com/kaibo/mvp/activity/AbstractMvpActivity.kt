package com.kaibo.mvp.activity

import android.os.Bundle
import com.kaibo.common.activity.BaseActivity
import com.kaibo.mvp.contract.BaseView
import com.kaibo.mvp.model.AbstractModel
import com.kaibo.mvp.presenter.AbstractPresenter
import com.kaibo.toast.ToastUtils
import java.lang.reflect.ParameterizedType

/**
 * @author Administrator
 * @date 2018/3/19 0019 上午 11:32
 * GitHub：
 * email：
 * description:
 * Activity  中使用    MVP
 */

abstract class AbstractMvpActivity<out P : AbstractPresenter<*, *>, out M : AbstractModel> :
        BaseActivity(), BaseView {

    protected val mPresenter: P

    init {
        val actualTypeArguments = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments

        //创建 presenter 实例  使用带 model view 参数的构造方法
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

    //------------------------绑定生命周期------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.onCreate(savedInstanceState)
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
        mPresenter.onStart()
        super.onStop()
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

}