package com.kaibo.ui.drawable

import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author:Administrator
 * @date:2018/5/30 0030 下午 4:43
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */
class ClockDrawable : Drawable(), Animatable {

    private val handler = DelayHandler(this)

    private var hour = 0
    private var minute = 0
    private var second = 0

    private val cx by lazy {
        bounds.exactCenterX()
    }

    private val cy by lazy {
        bounds.exactCenterY()
    }

    //表盘参数
    private var plateRadius = 340F
    private var plateLineLength = 30F
    private var plateLineWith = 10F

    //时针参数
    private var hourLineLength = 140F
    private var hourLineWidth = 20F
    //分针参数
    private var minuteLineLength = 210F
    private var minuteLineWidth = 15F
    //秒针参数
    private var secondLineLength = 240F
    private var secondLineWidth = 10F

    //画笔
    private val mPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeCap = Paint.Cap.ROUND
        strokeWidth = plateLineWith
        style = Paint.Style.FILL
    }

    //绘制文字的画笔
    private val mTextPaint = Paint().apply {
        set(mPaint)
        //字体大小
        textSize = 50F
    }

    /**
     * 存储表盘上的数字及其对应的坐标
     */
    private val textMap: Map<String, Pair<Float, Float>> by lazy {
        val map = LinkedHashMap<String, Pair<Float, Float>>()

        val angle = 2.0 * Math.PI / 12
        (-2 until 10).forEach {
            val key = (it + 3).toString()

            val rect = Rect()
            //测量文字宽高
            mTextPaint.getTextBounds(key, 0, key.length, rect)

            //坐标
            val x = (cx + (plateRadius - 60F) * Math.cos(angle * it)).toFloat() - rect.exactCenterX()
            val y = (cy + (plateRadius - 60F) * Math.sin(angle * it)).toFloat() - rect.exactCenterY()

            map[key] = Pair(x, y)
        }
        map
    }

    private var mRunning = false

    override fun isRunning() = mRunning

    override fun start() {
        mRunning = true
        handler.sendEmptyMessage(0)
    }

    override fun stop() {
        mRunning = false
        invalidateSelf()
    }

    /**
     * 绘制表盘上的数字
     */
    private fun drawText(canvas: Canvas) {
        textMap.forEach { key, value ->
            //绘制文字
            canvas.drawText(key, value.first, value.second, mTextPaint)
        }
    }

    /**
     * 绘制表盘
     */
    private fun drawPlate(canvas: Canvas) {
        (0 until 60).forEach {
            canvas.save()
            canvas.rotate(it * secondRangeStep, cx, cy)
            mPaint.color = Color.BLACK
            if (it % 5 == 0) {
                //整点的刻度加粗加长
                mPaint.strokeWidth = plateLineWith + 6F
                canvas.drawLine(cx + plateRadius - 20F, cy, cx + plateRadius + plateLineLength, cy, mPaint)
            } else {
                //非整点的刻度
                mPaint.strokeWidth = plateLineWith
                canvas.drawLine(cx + plateRadius, cy, cx + plateRadius + plateLineLength, cy, mPaint)
            }
            canvas.restore()
        }

        //绘制数字
        drawText(canvas)
    }

    /**
     * 绘制表的指针
     */
    private fun drawCursor(canvas: Canvas) {
        //画时
        mPaint.strokeWidth = hourLineWidth
        mPaint.color = Color.BLACK
        canvas.save()
        canvas.rotate((hour * 60 * 60 + minute * 60 + second) * hourRangeStep - 90, cx, cy)
        canvas.drawLine(cx - 40F, cy, cx + hourLineLength, cy, mPaint)
        canvas.restore()

        //画分
        mPaint.strokeWidth = minuteLineWidth
        mPaint.color = Color.CYAN
        canvas.save()
        canvas.rotate((minute * 60 + second) * minuteRangeStep - 90, cx, cy)
        canvas.drawLine(cx - 60F, cy, cx + minuteLineLength, cy, mPaint)
        canvas.restore()

        //画秒
        mPaint.strokeWidth = secondLineWidth
        mPaint.color = Color.RED
        canvas.save()
        canvas.rotate(second * secondRangeStep - 90, cx, cy)
        canvas.drawLine(cx - 80F, cy, cx + secondLineLength, cy, mPaint)
        canvas.restore()
    }

    override fun draw(canvas: Canvas) {
        drawPlate(canvas)
        drawCursor(canvas)

        //画中心的圆点
        canvas.drawCircle(bounds.exactCenterX(), bounds.exactCenterX(), 30F, mPaint.apply {
            color = Color.RED
        })
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
        invalidateSelf()
    }

    companion object {
        //秒针每秒的步长
        const val secondRangeStep = 360 / 60F
        //分针每秒的步长
        const val minuteRangeStep = 360 / (60 * 60F)
        //时针每秒的步长
        const val hourRangeStep = 360 / (12 * 60 * 60F)
    }

    private class DelayHandler(clockDrawable: ClockDrawable) : Handler() {

        private val weakReference = WeakReference<ClockDrawable>(clockDrawable)

        override fun handleMessage(msg: Message) {
            val clockDrawable: ClockDrawable? = weakReference.get()

            //是否继续绘制
            if (clockDrawable?.mRunning == true) {
                this.sendEmptyMessageDelayed(0, 1000L)
            } else {
                return
            }

            //获取当前的时间
            val calendar: Calendar = Calendar.getInstance()
            clockDrawable.hour = calendar.get(Calendar.HOUR)
            clockDrawable.minute = calendar.get(Calendar.MINUTE)
            clockDrawable.second = calendar.get(Calendar.SECOND)
            //触发重绘
            clockDrawable.invalidateSelf()
        }
    }
}
