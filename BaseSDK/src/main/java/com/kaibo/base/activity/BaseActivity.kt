package com.kaibo.base.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.kaibo.base.util.immersive

/**
 * @author Administrator
 * @date 2018/4/2 0002 上午 10:35
 * GitHub：
 * email：
 * description：
 */

abstract class BaseActivity : AppCompatActivity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //默认设置Activity为沉浸式
        immersive(null, false)

        setContentViewBefore(savedInstanceState)
        setContentView(getLayoutRes())
        setContentViewAfter(savedInstanceState)
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    /**
     * setContentView之前调用
     * 可以对window等做一些操作
     */
    protected open fun setContentViewBefore(savedInstanceState: Bundle?) {

    }

    /**
     * setContentView之后调用
     * 可以做一些初始化操作
     */
    protected open fun setContentViewAfter(savedInstanceState: Bundle?) {

    }
}