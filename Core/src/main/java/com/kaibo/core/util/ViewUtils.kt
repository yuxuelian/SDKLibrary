package com.kaibo.core.util

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.animation.Interpolator
import android.widget.Scroller

/**
 * @author:Administrator
 * @date:2018/6/11 0011上午 10:48
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

internal class FixedSpeedScroller constructor(context: Context, private val mDuration: Int) :
        Scroller(context, FixedSpeedScroller.sInterpolator) {

    companion object {
        val sInterpolator = Interpolator { t ->
            var t = t
            t -= 1.0f
            t * t * t * t * t + 1.0f
        }
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }
}

fun ViewPager.setDuration(duration: Int) {
    ViewPager::class.java
            .getDeclaredField("mScroller")
            .apply {
                isAccessible = true
                set(this@setDuration, FixedSpeedScroller(this@setDuration.context, duration))
            }
}