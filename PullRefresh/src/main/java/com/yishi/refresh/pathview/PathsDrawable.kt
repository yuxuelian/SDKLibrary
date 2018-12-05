package com.yishi.refresh.pathview

import android.graphics.*
import android.graphics.drawable.Drawable
import java.util.*

/**
 * @author 56896
 * @date 2018/10/18 23:54
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */
class PathsDrawable : Drawable() {

    private val paint: Paint = Paint()
    private var mPaths: List<Path>? = null
    private var mColors: MutableList<Int>? = null
    private var mWidth = 1
    private var mHeight = 1
    private var mStartX = 0
    private var mStartY = 0
    private var mOrginWidth: Int = 0
    private var mOrginHeight: Int = 0
    private var mOriginPaths: MutableList<Path>? = null
    private var mOriginSvg: MutableList<String>? = null
    private var mCachedBitmap: Bitmap? = null
    private var mCacheDirty: Boolean = false

    init {
        paint.color = -0xcfcfd0
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
    }

    private fun onMeasure() {
        var top: Int? = null
        var left: Int? = null
        var right: Int? = null
        var bottom: Int? = null
        if (mPaths != null) {
            for (path in mPaths!!) {
                REGION.setPath(path, MAX_CLIP)
                val bounds = REGION.bounds
                top = Math.min(top ?: bounds.top, bounds.top)
                left = Math.min(left ?: bounds.left, bounds.left)
                right = Math.max(right ?: bounds.right, bounds.right)
                bottom = Math.max(bottom ?: bounds.bottom, bounds.bottom)
            }
        }
        mStartX = left ?: 0
        mStartY = top ?: 0
        mWidth = if (right == null) 0 else right - mStartX
        mHeight = if (bottom == null) 0 else bottom - mStartY
        if (mOrginWidth == 0) {
            mOrginWidth = mWidth
        }
        if (mOrginHeight == 0) {
            mOrginHeight = mHeight
        }
        val bounds = bounds
        super.setBounds(bounds.left, bounds.top, bounds.left + mWidth, bounds.top + mHeight)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        val height = bottom - top
        if (mOriginPaths != null && mOriginPaths!!.size > 0 &&
                (width != mWidth || height != mHeight)) {
            val ratioWidth = 1f * width / mOrginWidth
            val ratioHeight = 1f * height / mOrginHeight
            mPaths = PathParser.transformScale(ratioWidth, ratioHeight, mOriginPaths!!, mOriginSvg)
            onMeasure()
        } else {
            super.setBounds(left, top, right, bottom)
        }
    }

    override fun setBounds(bounds: Rect) {
        setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
    }

    fun parserPaths(vararg paths: String) {
        mOrginHeight = 0
        mOrginWidth = mOrginHeight
        mOriginSvg = ArrayList()
        mOriginPaths = ArrayList()
        mPaths = mOriginPaths
        for (path in paths) {
            mOriginSvg!!.add(path)
            mOriginPaths!!.add(PathParser.createPathFromPathData(path))
        }
        onMeasure()
    }

    fun parserColors(vararg colors: Int) {
        mColors = ArrayList()
        for (color in colors) {
            mColors!!.add(color)
        }
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val width = bounds.width()
        val height = bounds.height()
        if (paint.alpha == 0xFF) {
            canvas.save()
            canvas.translate((bounds.left - mStartX).toFloat(), (bounds.top - mStartY).toFloat())
            if (mPaths != null) {
                for (i in mPaths!!.indices) {
                    if (mColors != null && i < mColors!!.size) {
                        paint.color = mColors!![i]
                    }
                    canvas.drawPath(mPaths!![i], paint)
                }
                paint.alpha = 0xFF
            }
            canvas.restore()
        } else {
            createCachedBitmapIfNeeded(width, height)
            if (!canReuseCache()) {
                updateCachedBitmap(width, height)
                updateCacheStates()
            }
            canvas.drawBitmap(mCachedBitmap!!, bounds.left.toFloat(), bounds.top.toFloat(), paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun width(): Int {
        return bounds.width()
    }

    fun height(): Int {
        return bounds.height()
    }

    fun setGeometricWidth(width: Int) {
        val bounds = bounds
        val rate = 1f * width / bounds.width()
        setBounds(
                (bounds.left * rate).toInt(),
                (bounds.top * rate).toInt(),
                (bounds.right * rate).toInt(),
                (bounds.bottom * rate).toInt()
        )

    }

    fun setGeometricHeight(height: Int) {
        val bounds = bounds
        val rate = 1f * height / bounds.height()
        setBounds(
                (bounds.left * rate).toInt(),
                (bounds.top * rate).toInt(),
                (bounds.right * rate).toInt(),
                (bounds.bottom * rate).toInt()
        )
    }

    private fun updateCachedBitmap(width: Int, height: Int) {
        mCachedBitmap!!.eraseColor(Color.TRANSPARENT)
        val tmpCanvas = Canvas(mCachedBitmap!!)
        drawCachedBitmap(tmpCanvas)
    }

    private fun drawCachedBitmap(canvas: Canvas) {
        canvas.translate((-mStartX).toFloat(), (-mStartY).toFloat())
        if (mPaths != null) {
            for (i in mPaths!!.indices) {
                if (mColors != null && i < mColors!!.size) {
                    paint.color = mColors!![i]
                }
                canvas.drawPath(mPaths!![i], paint)
            }
        }
    }

    private fun createCachedBitmapIfNeeded(width: Int, height: Int) {
        if (mCachedBitmap == null || !canReuseBitmap(width, height)) {
            mCachedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            mCacheDirty = true
        }
    }

    private fun canReuseBitmap(width: Int, height: Int): Boolean {
        return width == mCachedBitmap!!.width && height == mCachedBitmap!!.height
    }

    private fun canReuseCache(): Boolean {
        return !mCacheDirty
    }

    private fun updateCacheStates() {
        mCacheDirty = false
    }

    companion object {
        private val REGION = Region()
        private val MAX_CLIP = Region(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
    }
}
