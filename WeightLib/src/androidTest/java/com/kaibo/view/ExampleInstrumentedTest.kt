package com.kaibo.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.animation.LinearInterpolator
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        assertEquals("com.kaibo.weightlib.test", appContext.packageName)
    }

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
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Looper.prepare()
        mAnimator.start()
        Looper.loop()
    }
}
