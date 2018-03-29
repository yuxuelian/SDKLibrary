package com.kaibo.base.activity

import android.os.Bundle
import com.kaibo.base.R
import com.kaibo.base.mvp.model.AbstractModel
import com.kaibo.base.mvp.presenter.AbstractPresenter
import com.kaibo.base.mvp.view.AbstractFragment
import com.kaibo.base.util.ActivityUtils
import java.lang.reflect.ParameterizedType

/**
 * @author Administrator
 * @date 2018/3/19 0019 上午 11:32
 * GitHub：
 * email：
 * description：使用MVP的Activity继承
 */


abstract class BaseMvpActivity<M : AbstractModel, V : AbstractFragment<*>, P : AbstractPresenter<*, *>> : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewPresenter()
    }

    override fun getLayoutRes() = R.layout.activity_mvp_layout

    @SuppressWarnings("unchecked")
    private fun bindViewPresenter() {
        var thisClass: Class<*> = this.javaClass
        while (true) {
            (thisClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.let {
                if (it.size == 3) {
                    //创建model实例
                    val modelClass = it[0] as Class<M>
                    val model = modelClass.newInstance()

                    //创建view实例
                    val viewClass = it[1] as Class<V>
                    val view = viewClass.newInstance()

                    //创建presenter实例  使用带model参数的构造方法
                    val presenter = (it[2] as Class<P>).getConstructor(modelClass, viewClass).newInstance(model, view)

                    //给view设置presenter
                    view.setPresenter(presenter)

                    //将view实例添加到activity
                    supportFragmentManager.findFragmentById(R.id.mvpViewContainer)
                            ?: ActivityUtils.addFragmentToActivity(supportFragmentManager, view, R.id.mvpViewContainer)
                    return
                } else {
                    throw IllegalArgumentException("泛型参数有误")
                }
            } ?: run {
                thisClass = thisClass.superclass ?: throw IllegalArgumentException("始终没有找到泛型参数")
            }
        }
    }
}