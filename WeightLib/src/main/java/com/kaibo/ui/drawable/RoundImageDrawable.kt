package com.kaibo.ui.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * @author:Administrator
 * @date:2018/5/29 0029 上午 9:46
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

class RoundImageDrawable(private val mBitmap: Bitmap, private val mRadius: Float) : Drawable() {

    private val mPaint = Paint()
    private val mRectF = RectF()

    init {
        mPaint.apply {
            isAntiAlias = true
            val bitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader = bitmapShader
        }
    }

    override fun getIntrinsicWidth(): Int {
        return mBitmap.width
    }

    override fun getIntrinsicHeight(): Int {
        return mBitmap.height
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        mRectF.set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

}