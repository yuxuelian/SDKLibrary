package com.kaibo.ui.weight

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * @author:Administrator
 * @date:2018/5/28 0028 上午 9:28
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */
class MagicPathView @JvmOverloads constructor(mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(mContext, attrs, defStyleAttr) {

    //the View center pointer
    private val centerPoint: Point = Point()

    //画笔
    private val linePaint: Paint = Paint()
    private val dotPaint: Paint = Paint()

    private val pathList: MutableList<Polygon> = ArrayList()

    var dotProgress: Float = 0F
        set(value) {
            field = value.coerceIn(0F, 1F)
            invalidate()
        }

    private val circlePath: Path = Path()

    val dotAnimation: ValueAnimator = ValueAnimator.ofFloat(1F)
            .apply {
                duration = 10000
                interpolator = LinearInterpolator()
                this.repeatCount = ValueAnimator.INFINITE
                addUpdateListener {
                    //更新 dotProgress
                    dotProgress = it.animatedValue as Float
                }
            }

    init {
        linePaint.apply {
            strokeWidth = 2F
            style = Paint.Style.STROKE
            isAntiAlias = true
            //圆角化
            pathEffect = CornerPathEffect(20F)
        }

        dotPaint.apply {
            //复制一个Paint
            set(linePaint)
            style = Paint.Style.FILL
            color = Color.BLUE
        }

        circlePath.addCircle(0F, 0F, 8F, Path.Direction.CW)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerPoint.x = w / 2
        centerPoint.y = h / 2
        pathList.clear()
        //初始化一些路径
        (0..5).forEach {
            pathList.add(Polygon(centerPoint, 3 + it, 100F + it * 40F, Color.RED, 6 - it))
        }
    }

    override fun onDraw(canvas: Canvas) {
        pathList.forEach {
            linePaint.color = it.color
//            canvas.drawPath(it.getPathSegment(dotProgress), linePaint)
            canvas.drawPath(it.path, linePaint)
        }

        pathList.forEach {
            val phase = (1.0F - dotProgress) * (it.length * it.laps)

            @SuppressLint("DrawAllocation")
            dotPaint.pathEffect = PathDashPathEffect(circlePath, it.length, phase, PathDashPathEffect.Style.ROTATE)

            //绘制
            canvas.drawPath(it.path, dotPaint)
        }
    }

    private class Polygon(val point: Point, sides: Int, radius: Float, @ColorInt val color: Int, val laps: Int) {

        companion object {
            private val tempPathMeasure: PathMeasure = PathMeasure()
            private val tempPath: Path = Path()
        }

        val path = createPath(sides, radius)

        //获取路径总长度
        val length by lazy {
            tempPathMeasure.setPath(path, false)
            tempPathMeasure.length
        }

        /**
         * 指定进度获取Path
         */
        fun getPathSegment(dotProgress: Float): Path {
            //重置
            tempPathMeasure.setPath(path, false)
            tempPath.reset()

            //获取线段 到 tempPath
            tempPathMeasure.getSegment(0F, dotProgress * length, tempPath, true)
            if (dotProgress == 1F) {
                tempPath.close()
            }
            return tempPath
        }

        /**
         * 创建多边形路径
         */
        private fun createPath(sides: Int, radius: Float): Path {
            val path = Path()
            val angle = 2.0 * Math.PI / sides
            path.moveTo((point.x + radius * Math.cos(0.0)).toFloat(), (point.y + radius * Math.sin(0.0)).toFloat())
            (1 until sides).forEach {
                path.lineTo((point.x + radius * Math.cos(angle * it)).toFloat(), (point.y + radius * Math.sin(angle * it)).toFloat())
            }
            path.close()
            return path
        }
    }
}