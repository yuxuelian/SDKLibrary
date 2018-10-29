package com.kaibo.core.util

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.kaibo.core.R
import com.kaibo.core.adapter.ItemDividerDecoration
import org.jetbrains.anko.dip

/**
 * @author kaibo
 * @date 2018/9/21 15:50
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

/**
 * 设置默认的RecyclerView的分割线
 */
fun RecyclerView.addDefaultDecoration(decoration: Drawable = ColorDrawable(ContextCompat.getColor(context, R.color.color_303030)),
                                      heightPx: Int = dip(1)) {
    this.addItemDecoration(ItemDividerDecoration(decoration, heightPx))
}

fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean): View {
    return LayoutInflater.from(context).inflate(resource, this, attachToRoot)
}

/**
 * 获取View上的截图
 */
fun View.screenshot(): Bitmap {
    isDrawingCacheEnabled = true
    buildDrawingCache(true)
    val bitmap: Bitmap = Bitmap.createBitmap(this.drawingCache)
    isDrawingCacheEnabled = false
    return bitmap
}

fun ImageView.startAlphaBitmap(bitmap: Bitmap) {
    val fromDrawable = this.drawable ?: ColorDrawable(Color.TRANSPARENT)
    val toDrawable = BitmapDrawable(resources, bitmap)
    val transitionDrawable = TransitionDrawable(arrayOf(fromDrawable, toDrawable))
    this.setImageDrawable(transitionDrawable)
    transitionDrawable.startTransition(300)
}

fun ImageView.startAlphaDrawable(toDrawable: Drawable) {
    val fromDrawable = this.drawable ?: ColorDrawable(Color.TRANSPARENT)
    val td = TransitionDrawable(arrayOf(fromDrawable, toDrawable))
    this.setImageDrawable(td)
    td.startTransition(300)
}
