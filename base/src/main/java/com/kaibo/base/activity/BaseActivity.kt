package com.kaibo.base.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author Administrator
 * @date 2018/3/16 0016 上午 11:41
 * GitHub：
 * email：
 * description：
 */

abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())

    }

}