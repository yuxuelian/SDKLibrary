package com.kaibo.base

import android.app.Application

/**
 * @author Administrator
 * @date 2018/3/16 0016 下午 1:45
 * GitHub：
 * email：
 * description：
 */
open class BaseApplication : Application() {

    companion object {
        val INSTANCE by lazy {
            tempInstance
        }
        private lateinit var tempInstance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        tempInstance = this

    }
}