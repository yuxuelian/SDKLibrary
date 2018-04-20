package com.kaibo.common

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.kaibo.common.util.ToastUtils
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
        val INSTANCE by lazy {
            tempInstance
        }
        private lateinit var tempInstance: BaseApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        tempInstance = this
        ToastUtils.init(this)
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}