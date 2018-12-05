package com.kaibo.swipebacklib.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import android.view.ViewGroup
import com.kaibo.core.activity.SuperActivity
import com.kaibo.swipebacklib.util.Utils
import com.kaibo.swipebacklib.weight.SwipeBackLayout

/**
 * @author:Administrator
 * @date:2018/4/2 0002 上午 10:35
 * GitHub:
 * email:
 * description:继承这个Activity可以轻松实现  侧滑返回
 */

abstract class BaseSwipeBackActivity : SuperActivity(), SwipeBackLayout.SwipeListener {

    private lateinit var swipeBackLayout: SwipeBackLayout

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置Windows背景为透明
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 清除 decorView 背景
        window.decorView.background = null

        swipeBackLayout = SwipeBackLayout(this)
        swipeBackLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        // 设置只能从左边滑动
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)

        // 滑动监听
        swipeBackLayout.addSwipeListener(this)
    }

    override fun onScrollStateChange(state: Int, scrollPercent: Float) {
        if (SwipeBackLayout.STATE_IDLE == state) {
            Utils.convertActivityFromTranslucent(this@BaseSwipeBackActivity)
        }
    }

    override fun onEdgeTouch(edgeFlag: Int) {
        Utils.convertActivityToTranslucent(this@BaseSwipeBackActivity)
    }

    override fun onScrollOverThreshold() {
    }

    @CallSuper
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        swipeBackLayout.attachToActivity(this)
    }

    override fun <T : View> findViewById(id: Int): T? {
        return super.findViewById(id) ?: swipeBackLayout.findViewById(id)
    }

//    override fun onBackPressed() {
//        Utils.convertActivityToTranslucent(this)
//        swipeBackLayout.scrollToFinishActivity()
//    }
}
