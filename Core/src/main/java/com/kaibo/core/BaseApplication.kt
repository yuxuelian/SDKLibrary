package com.kaibo.core

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
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
        val baseApplication by lazy {
            this.INSTANCE
        }
        private lateinit var INSTANCE: BaseApplication
    }

    override fun attachBaseContext(base: Context?) {
        MultiDex.install(this)
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        ToastUtils.init(this)
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}