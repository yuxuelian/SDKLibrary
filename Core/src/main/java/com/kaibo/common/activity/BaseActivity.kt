package com.kaibo.common.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.kaibo.common.util.immersive

/**
 * @author Administrator
 * @date 2018/4/2 0002 上午 10:35
 * GitHub：
 * email：
 * description：
 */

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 是否沉浸式   Pair  的第一个参数表示是否沉浸式
     * 第二个参数    当第一个参数为true的时候  第二个参数才生效
     * 第二个参数表示是否使状态栏的颜色变成黑色
     */
    protected open fun enableImmersive(): Pair<Boolean, Boolean> = Pair(true, false)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //默认设置Activity为沉浸式
        val (enableImmersive, isLight) = enableImmersive()
        if (enableImmersive) {
            immersive(isLight)
        }

        setContentViewBefore(savedInstanceState)
        setContentView(getLayoutRes())
        initOnCreate(savedInstanceState)
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
    protected open fun initOnCreate(savedInstanceState: Bundle?) {

    }
}