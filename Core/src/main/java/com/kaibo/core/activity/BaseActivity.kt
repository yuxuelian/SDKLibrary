package com.kaibo.core.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.WindowManager
import com.kaibo.core.R
import com.kaibo.core.dialog.LoadingDialog
import com.kaibo.core.util.bindToAutoDispose
import com.kaibo.core.util.immersive
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.autodispose.AutoDisposeConverter

/**
 * @author:Administrator
 * @date:2018/4/2 0002 上午 10:35
 * GitHub:
 * email:
 * description:
 */

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 给每个Activity定义一个 rxPermissions 方便动态权限申请
     */
    protected val rxPermissions by lazy {
        RxPermissions(this)
    }

    private val loadingDialog by lazy {
        LoadingDialog()
    }

    protected fun showLoading() {
        if (!loadingDialog.isVisible) {
            loadingDialog.show(supportFragmentManager)
        }
    }

    protected fun hideLoading() {
        if (loadingDialog.isVisible) {
            loadingDialog.hide()
        }
    }

    /**
     * 是否沉浸式   Pair  的第一个参数表示是否沉浸式
     * 第二个参数    当第一个参数为true的时候  第二个参数才生效
     * 第二个参数表示是否使状态栏的颜色变成黑色
     */
    protected open fun enableImmersive(): Pair<Boolean, Boolean> = Pair(true, false)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //禁止应用内截屏
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        //禁止横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //默认设置Activity为沉浸式
        val (enableImmersive, isLight) = enableImmersive()
        if (enableImmersive) {
            immersive(isLight)
        }
        setContentViewBefore(savedInstanceState)
        setContentView(getLayoutRes())
        initOnCreate(savedInstanceState)
    }

    /**
     * 初始化toolBar标题中的一些操作
     */
    protected fun initSupportActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        //不显示标题,显示返回按钮
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }
        //点击Navigation按钮
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    /**
     * 带动画返回
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.translation_left_in, R.anim.translation_left_out)
    }

    /**
     * 带动画结束
     */
    protected fun animFinish() {
        finish()
        overridePendingTransition(R.anim.translation_left_in, R.anim.translation_left_out)
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

//    override fun attachBaseContext(newBase: Context) {
//        super.attachBaseContext(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            newBase.changeLanguage("zh")
//        } else {
//            newBase
//        })
//    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1F) {
            //强制设置为1F
            newConfig.fontScale = 1F
        }
        super.onConfigurationChanged(newConfig)
    }

    /**
     * Rx绑定生命周期
     */
    protected fun <T> bindLifecycle(): AutoDisposeConverter<T> = bindToAutoDispose(this)
}
