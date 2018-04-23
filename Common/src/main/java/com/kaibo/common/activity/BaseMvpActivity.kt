package com.kaibo.common.activity

import android.os.Bundle
import com.kaibo.common.R
import com.kaibo.common.mvp.model.AbstractModel
import com.kaibo.common.mvp.presenter.AbstractPresenter
import com.kaibo.common.mvp.view.AbstractFragment
import com.kaibo.common.util.addFragmentToActivity
import com.orhanobut.logger.Logger
import java.lang.reflect.ParameterizedType

/**
 * @author Administrator
 * @date 2018/3/19 0019 上午 11:32
 * GitHub：
 * email：
 * description：使用MVP的Activity继承
 * 这个类的主要功能是  通过反射  完成对  MVP三者之间的关系  进行  绑定操作
 */

abstract class BaseMvpActivity<out M : AbstractModel,
        out V : AbstractFragment<*>,
        out P : AbstractPresenter<*, *>> :
        RxActivity() {

    protected val model: M
        get() = _model

    protected val view: V
        get() = _view

    protected val presenter: P
        get() = _presenter

    private lateinit var _model: M
    private lateinit var _view: V
    private lateinit var _presenter: P

    init {
        var thisClass: Class<*> = this.javaClass
        var findFlag = false
        while (!findFlag) {
            (thisClass.genericSuperclass as? ParameterizedType)
                    ?.actualTypeArguments
                    ?.let {
                        if (it.size == 3) {
                            //创建 model 实例
                            @Suppress("UNCHECKED_CAST")
                            val modelClass = it[0] as Class<M>
                            _model = modelClass.newInstance()

                            //创建 view 实例
                            @Suppress("UNCHECKED_CAST")
                            val viewClass = it[1] as Class<V>
                            _view = viewClass.newInstance()

                            //创建 presenter 实例  使用带 model view 参数的构造方法
                            @Suppress("UNCHECKED_CAST")
                            _presenter = (it[2] as Class<P>)
                                    .getConstructor(modelClass, viewClass)
                                    .newInstance(_model, _view)

                            //给 view 设置 presenter
                            _view.mPresenter = _presenter
                            findFlag = true
                        } else {
                            throw IllegalArgumentException("泛型参数个数有误")
                        }
                    }
                    ?: run {
                        thisClass = thisClass.superclass ?: throw IllegalArgumentException("始终没有找到泛型参数")
                    }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //将view实例添加到activity
        this.addFragmentToActivity(R.id.mvpViewContainer, _view)

    }

    override fun getLayoutRes() = R.layout.activity_mvp_layout
}