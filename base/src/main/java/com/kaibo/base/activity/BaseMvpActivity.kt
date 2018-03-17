package com.kaibo.base.activity

import com.kaibo.base.mvp.BaseView
import com.kaibo.base.mvp.abs.AbstractMvpPresenter
import java.lang.reflect.ParameterizedType

/**
 * @author Administrator
 * @date 2018/3/17 16:33
 * GitHub：
 * email：
 * description：完成对Presenter的实例化  以及  绑定View
 */
abstract class BaseMvpActivity<out P : AbstractMvpPresenter<BaseMvpActivity<P>>> : BaseActivity(), BaseView<P> {

    override val mPresenter: P by lazy {
        findPresenterClass()
                .newInstance()
                .apply {
                    //实例化
                    view = this@BaseMvpActivity
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        //解除绑定
        mPresenter.view = null
    }

    override fun showLoadView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoadView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun findPresenterClass(): Class<P> {
        var thisClass: Class<*> = this.javaClass
        while (true) {
            (thisClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.firstOrNull()?.let {
                return it as Class<P>
            } ?: run {
                thisClass = thisClass.superclass ?: throw IllegalArgumentException("始终没有找到泛型参数")
            }
        }
    }
}