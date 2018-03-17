package com.kaibo.base.mvp.abs

import com.kaibo.base.activity.BaseMvpActivity
import com.kaibo.base.mvp.BasePresenter

/**
 * @author Administrator
 * @date 2018/3/17 16:33
 * GitHub：
 * email：
 * description：完成View 的绑定
 * 要求子类必须要有一个无参的构造方法   否则会报错
 */

abstract class AbstractMvpPresenter<out V : BaseMvpActivity<AbstractMvpPresenter<V>>> : BasePresenter<V> {

    override var view: @UnsafeVariance V? = null


}