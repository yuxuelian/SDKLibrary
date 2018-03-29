package com.kaibo.base.mvp.presenter

import com.kaibo.base.mvp.model.BaseModel
import com.kaibo.base.mvp.view.BaseView

/**
 * @author Administrator
 * @date 2018/3/17 16:33
 * GitHub：
 * email：
 * description：完成View 的绑定
 * 要求子类必须要有一个无参的构造方法   否则会报错
 */

abstract class AbstractPresenter<out V : BaseView<BasePresenter<V>>, out M : BaseModel>(protected val mModel: M, protected val mView:V) : BasePresenter<V> {

    override fun onResume() {
        mView.showToast("执行了  onResume")
    }

    override fun onDestroy() {
//        mView = null
        mView.showToast("执行了  onDestroy")
    }
}