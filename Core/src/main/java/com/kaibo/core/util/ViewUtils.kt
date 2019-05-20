package com.kaibo.core.util

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.graphics.drawable.DrawableCompat
import com.kaibo.core.R
import com.kaibo.core.error.handle
import com.kaibo.core.error.handleHttpException
import com.kaibo.core.http.DownLoadApi
import retrofit2.HttpException
import java.lang.ref.WeakReference

/**
 * @author kaibo
 * @date 2018/9/21 15:50
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View = LayoutInflater.from(context).inflate(resource, this, attachToRoot)

/**
 * 获取View上的截图
 */
fun View.screenshot(): Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
    draw(Canvas(it))
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

/**
 * 给ImageView上已经存在的Drawable染色
 */
fun ImageView.setTintDrawable(@ColorInt color: Int) {
    this.drawable?.let {
        DrawableCompat.setTintList(it, ColorStateList.valueOf(color))
        this.setImageDrawable(it)
    }
}

/**
 * 缓存bitmap  并且使用弱引用的方式(防止oom)
 */
private val mLruBitmapCache = LruCache<String, WeakReference<Bitmap>>(20)

/**
 * 带缓存设置图片到ImageView
 * 有缓存是直接从缓存加载
 * 无缓存时从网络加载
 *
 * onlyNet 是否仅从网络加载
 */
fun ImageView.setImageUrlAndCache(url: String,
                                  onlyNet: Boolean = false,
                                  @DrawableRes placeholder: Int = R.drawable.header_icon_placeholder) {
    // 首先获取一下url对应的Bitmap是否存在
    val cacheBitmap: Bitmap? = mLruBitmapCache.get(url)?.get()
    if (cacheBitmap != null && !onlyNet) {
        this.setImageBitmap(cacheBitmap)
    } else {
        // 若缓存的bitmap已经被回收  这里需要将  WeakReference  对象也一并移出
        mLruBitmapCache.remove(url)
        launchUI {
            try {
                val responseBody = DownLoadApi.instance.downLoadFileAsync(url).await()
                responseBody.byteStream()
                        ?.let {
                            // 解析数据
                            val resBitmap: Bitmap = BitmapFactory.decodeStream(it)
                            // 存储到缓存中
                            mLruBitmapCache.put(url, WeakReference(resBitmap))
                            resBitmap
                        }
                        ?.let {
                            // 显示
                            setImageBitmap(it)
                        }
            } catch (e: Exception) {
                if (e is HttpException) {
                    // 头像加载失败
                    context.handleHttpException(e)
                    setImageResource(placeholder)
                } else {
                    context.handle(e)
                }
            }
        }.asAutoDisposable(this)
    }
}
