package com.kaibo.view.drawable

import android.content.res.Resources
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
class ClockDrawable(val resources: Resources) : Drawable(), Animatable {

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
    private var plateRadius = dp(140F)
    private var plateLineLength = dp(10F)
    private var plateLineWith = dp(4F)

    //时针参数
    private var hourLineLength = dp(50F)
    private var hourLineWidth = dp(10F)
    //分针参数
    private var minuteLineLength = dp(75F)
    private var minuteLineWidth = dp(6F)
    //秒针参数
    private var secondLineLength = dp(100F)
    private var secondLineWidth = dp(2F)

    private fun dp(dp: Float): Float {
        return resources.displayMetrics.density * dp
    }

    private fun sp(sp: Float): Float {
        return resources.displayMetrics.scaledDensity * sp
    }

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
        textSize = sp(20F)
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
            val x = (cx + (plateRadius - dp(30F)) * Math.cos(angle * it)).toFloat() - rect.exactCenterX()
            val y = (cy + (plateRadius - dp(30F)) * Math.sin(angle * it)).toFloat() - rect.exactCenterY()

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
        textMap.entries.forEach { entry: Map.Entry<String, Pair<Float, Float>> ->
            //绘制文字
            canvas.drawText(entry.key, entry.value.first, entry.value.second, mTextPaint)
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
                mPaint.strokeWidth = plateLineWith + dp(3F)
                canvas.drawLine(cx + plateRadius - dp(10F), cy, cx + plateRadius + plateLineLength, cy, mPaint)
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
        canvas.drawLine(cx - dp(20F), cy, cx + hourLineLength, cy, mPaint)
        canvas.restore()

        //画分
        mPaint.strokeWidth = minuteLineWidth
        mPaint.color = Color.CYAN
        canvas.save()
        canvas.rotate((minute * 60 + second) * minuteRangeStep - 90, cx, cy)
        canvas.drawLine(cx - dp(30F), cy, cx + minuteLineLength, cy, mPaint)
        canvas.restore()

        //画秒
        mPaint.strokeWidth = secondLineWidth
        mPaint.color = Color.RED
        canvas.save()
        canvas.rotate(second * secondRangeStep - 90, cx, cy)
        canvas.drawLine(cx - dp(40F), cy, cx + secondLineLength, cy, mPaint)
        canvas.restore()
    }

    override fun draw(canvas: Canvas) {
        drawPlate(canvas)
        drawCursor(canvas)

        //画中心的圆点
        canvas.drawCircle(bounds.exactCenterX(), bounds.exactCenterX(), dp(10F), mPaint.apply {
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
}
