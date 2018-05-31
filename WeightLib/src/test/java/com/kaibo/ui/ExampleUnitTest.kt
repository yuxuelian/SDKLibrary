package com.kaibo.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {

    //执行动画
    private val mAnimator by lazy {
        ValueAnimator.ofInt(Int.MAX_VALUE)
                .apply {
                    duration = 1000L
                    interpolator = LinearInterpolator()
                    this.repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.RESTART
                    addUpdateListener {
                        //触发重绘
                        println(it.animatedValue as Int)
                    }
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationRepeat(animation: Animator) {

                        }
                    })
                }
    }


    @Test
    fun addition_isCorrect() {
        val calendar: Calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        println(hour)
        val minute = calendar.get(Calendar.MINUTE)
        println(minute)
        val second = calendar.get(Calendar.SECOND)
        println(second)
    }


}