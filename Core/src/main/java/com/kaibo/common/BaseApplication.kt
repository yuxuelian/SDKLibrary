package com.kaibo.common

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.kaibo.common.toast.ToastUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:45
 * GitHub：
 * email：
 * description：
 */
abstract class BaseApplication : Application() {

    companion object {
        val Instance by lazy {
            _instance
        }
        private lateinit var _instance: BaseApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
        ToastUtils.init(this)
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}