package com.kaibo.core.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kaibo.core.R
import com.kaibo.core.toast.showError
import com.kaibo.core.util.bindLifecycle
import com.kaibo.core.util.immersive
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @author:Administrator
 * @date:2018/4/2 0002 上午 10:35
 * GitHub:
 * email:
 * description:
 */

abstract class SuperActivity : AppCompatActivity() {

    private val rxPermissions by lazy {
        RxPermissions(this)
    }

    /**
     * 封装一下权限申请后的处理逻辑
     */
    protected fun easyRequestPermission(vararg permissionNames: String, reject: (() -> Unit)? = null, resolve: () -> Unit) {
        // 过滤出需没有授权的权限
        val needRequest = permissionNames.filter { !rxPermissions.isGranted(it) }
        if (needRequest.isNotEmpty()) {
            // 请求权限
            rxPermissions.requestEach(*needRequest.toTypedArray())
                    .`as`(bindLifecycle())
                    .subscribe { permission: Permission ->
                        if (permission.granted) {
                            resolve.invoke()
                        } else {
                            reject?.invoke()
                                    ?: if (!permission.shouldShowRequestPermissionRationale) {
                                        this.showError("所需权限被拒绝,无法进行相关操作")
                                    } else {
                                        this.showError("所需权限被永久拒绝,请到安全中心开启")
                                    }
                        }
                    }
        } else {
            resolve.invoke()
        }
    }

    /**
     * 是否隐藏状态栏
     */
    protected open fun isHideStatusBar() = false

    /**
     * 是否沉浸式   Pair  的第一个参数表示是否沉浸式
     * 第二个参数    当第一个参数为true的时候  第二个参数才生效
     * 第二个参数表示是否使状态栏的颜色变成黑色
     */
    protected open fun enableImmersive() = Pair(true, false)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 禁止应用内截屏
//        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        // 禁止横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // 默认设置Activity为沉浸式
        val (enableImmersive, isLight) = enableImmersive()
        if (enableImmersive) {
            window.immersive(isLight)
        }
        // 设置为全屏
        if (isHideStatusBar()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
}