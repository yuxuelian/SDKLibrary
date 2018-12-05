package com.kaibo.swipebacklib.util

import android.app.Activity
import android.app.ActivityOptions
import android.os.Build


object Utils {

    fun convertActivityToTranslucent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertActivityToTranslucentAfterL(activity)
        } else {
            convertActivityToTranslucentBeforeL(activity)
        }
    }

    fun convertActivityFromTranslucent(activity: Activity) {
        try {
            val method = Activity::class.java.getDeclaredMethod("convertFromTranslucent")
            method.isAccessible = true
            method.invoke(activity)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun convertActivityToTranslucentAfterL(activity: Activity) {
        try {
            val getActivityOptions = Activity::class.java.getDeclaredMethod("getActivityOptions")
            getActivityOptions.isAccessible = true
            val options = getActivityOptions.invoke(activity)

            val classes = Activity::class.java.declaredClasses
            var translucentConversionListenerClazz: Class<*>? = null
            for (clazz in classes) {
                if (clazz.simpleName.contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz
                }
            }
            val convertToTranslucent = Activity::class.java.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions::class.java)
            convertToTranslucent.isAccessible = true
            convertToTranslucent.invoke(activity, null, options)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun convertActivityToTranslucentBeforeL(activity: Activity) {
        try {
            val classes = Activity::class.java.declaredClasses
            var translucentConversionListenerClazz: Class<*>? = null
            for (clazz in classes) {
                if (clazz.simpleName.contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz
                }
            }
            val method = Activity::class.java.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz)
            method.isAccessible = true
            method.invoke(activity)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}
