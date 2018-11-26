package com.kaibo.core

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import com.kaibo.core.http.HttpRequestManager
import com.kaibo.core.toast.ToastUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * @author:Administrator
 * @date:2018/3/16 0016 下午 1:45
 * GitHub:
 * email:
 * description:
 */
abstract class BaseApplication : Application() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        private lateinit var baseApplication: BaseApplication

        val INSTANCE: Application
            get() = baseApplication

        /**
         * 状态栏高
         */
        internal val statusBarHeight by lazy {
            INSTANCE.resources.let {
                it.getDimensionPixelSize(it.getIdentifier("status_bar_height", "dimen", "android"))
            }
        }

        /**
         * 设备宽
         */
        internal val deviceWidth by lazy {
            INSTANCE.resources.displayMetrics.widthPixels
        }

        /**
         * 设备高
         */
        internal val deviceHeight by lazy {
            INSTANCE.resources.displayMetrics.heightPixels
        }

    }

    override fun attachBaseContext(base: Context?) {
        MultiDex.install(this)
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        baseApplication = this

//        // 初始化配置BaseURL
//        HttpRequestManager.BASE_URL = getBaseUrl()

        // 初始化Toast
        ToastUtils.init(this)
        // 初始化Logger
        Logger.addLogAdapter(AndroidLogAdapter())
    }

//    protected open fun getBaseUrl(): String {
//        return ""
//    }

}