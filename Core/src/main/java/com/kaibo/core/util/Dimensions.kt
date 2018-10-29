package com.kaibo.core.util

import android.content.Context
import android.support.annotation.DimenRes
import android.support.v4.app.Fragment
import android.view.View

/**
 * @author kaibo
 * @date 2018/10/29 10:49
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

// 能获取到Content的情况
fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()
fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity
fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

// View中使用
inline fun View.dip(value: Int): Int = context.dip(value)

inline fun View.dip(value: Float): Int = context.dip(value)
inline fun View.sp(value: Int): Int = context.sp(value)
inline fun View.sp(value: Float): Int = context.sp(value)
inline fun View.px2dip(px: Int): Float = context.px2dip(px)
inline fun View.px2sp(px: Int): Float = context.px2sp(px)
inline fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)

// SupportFragment中使用
inline fun Fragment.dip(value: Int): Int = requireActivity().dip(value)

inline fun Fragment.dip(value: Float): Int = requireActivity().dip(value)
inline fun Fragment.sp(value: Int): Int = requireActivity().sp(value)
inline fun Fragment.sp(value: Float): Int = requireActivity().sp(value)
inline fun Fragment.px2dip(px: Int): Float = requireActivity().px2dip(px)
inline fun Fragment.px2sp(px: Int): Float = requireActivity().px2sp(px)
inline fun Fragment.dimen(resource: Int): Int = requireActivity().dimen(resource)

// 原生Fragment中使用
inline fun android.app.Fragment.dip(value: Int): Int = activity.dip(value)

inline fun android.app.Fragment.dip(value: Float): Int = activity.dip(value)
inline fun android.app.Fragment.sp(value: Int): Int = activity.sp(value)
inline fun android.app.Fragment.sp(value: Float): Int = activity.sp(value)
inline fun android.app.Fragment.px2dip(px: Int): Float = activity.px2dip(px)
inline fun android.app.Fragment.px2sp(px: Int): Float = activity.px2sp(px)
inline fun android.app.Fragment.dimen(resource: Int): Int = activity.dimen(resource)


