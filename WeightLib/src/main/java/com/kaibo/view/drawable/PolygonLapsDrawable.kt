package com.kaibo.view.drawable

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt

/**
 * @author:Administrator
 * @date:2018/5/28 0028 下午 2:00
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */
class PolygonLapsDrawable : Drawable(), Animatable {

    companion object {
        //多边形的数量
        private const val COUNT = 10
        private val LINEAR_INTERPOLATOR = LinearInterpolator()
    }

    //画笔
    private val mLinePaint: Paint = Paint()
    private val mDotPaint: Paint = Paint()

    //路径集合
    private val mPathList: MutableList<Polygon> = ArrayList()

    //动画执行进度控制变量
    private var mDotProgress: Float = 0F
        set(value) {
            field = value.coerceIn(0F, 1F)
            //触发重绘
            invalidateSelf()
        }

    //圆点路劲
    private val mCirclePath: Path = Path()

    //执行动画
    private val mAnimator by lazy {
        ValueAnimator.ofFloat(1F)
                .apply {
                    duration = 16000L
                    interpolator = LINEAR_INTERPOLATOR
                    this.repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.RESTART
                    addUpdateListener {
                        mDotProgress = it.animatedValue as Float
                    }
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationRepeat(animation: Animator) {

                        }
                    })
                }
    }

    init {
        //画线的画笔
        mLinePaint.apply {
            strokeWidth = 2F
            style = Paint.Style.STROKE
            isAntiAlias = true
            //圆角化
            pathEffect = CornerPathEffect(10F)
        }

        //圆圈的画笔
        mDotPaint.apply {
            //复制一个Paint
            set(mLinePaint)
            style = Paint.Style.FILL
            color = Color.BLUE
        }

        mCirclePath.addCircle(0F, 0F, 6F, Path.Direction.CW)
    }

    override fun isRunning() = mAnimator.isRunning

    override fun start() {
        mAnimator.cancel()
        mAnimator.start()
    }

    override fun stop() {
        mAnimator.cancel()
        invalidateSelf()
    }


    fun pause() {
        if (mAnimator.isRunning) {
            mAnimator.pause()
        }
    }

    fun resume() {
        if (mAnimator.isPaused) {
            mAnimator.resume()
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mPathList.clear()
        //初始化一些多边形路径
        (0..COUNT).forEach {
            mPathList.add(Polygon(bounds.exactCenterX(), bounds.exactCenterY(), 3 + it, 60F + it * 30F, Color.RED, COUNT + 1 - it))
        }
    }

    override fun draw(canvas: Canvas) {
        mPathList.forEach {
            mLinePaint.color = it.color
//            canvas.drawPath(it.getPathSegment(mDotProgress), mLinePaint)
            canvas.drawPath(it.path, mLinePaint)
        }

        mPathList.forEach {
            val phase = (1.0F - mDotProgress) * (it.length * it.laps)

            @SuppressLint("DrawAllocation")
            mDotPaint.pathEffect = PathDashPathEffect(mCirclePath, it.length, phase, PathDashPathEffect.Style.ROTATE)

            //绘制
            canvas.drawPath(it.path, mDotPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        mLinePaint.alpha = alpha
        mDotPaint.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        //TRANSLUCENT 半透明
        //OPAQUE 不透明
        //TRANSPARENT 透明
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mLinePaint.colorFilter = colorFilter
        mDotPaint.colorFilter = colorFilter
        invalidateSelf()
    }

    private class Polygon(val cx: Float, val cy: Float, sides: Int, radius: Float, @ColorInt val color: Int, val laps: Int) {

        companion object {
            private val tempPathMeasure: PathMeasure = PathMeasure()
            private val tempPath: Path = Path()
        }

        val path = createPath(sides, radius)

        //路径总长度
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
            path.moveTo((cx + radius * Math.cos(0.0)).toFloat(), (cy + radius * Math.sin(0.0)).toFloat())
            (1 until sides).forEach {
                path.lineTo((cx + radius * Math.cos(angle * it)).toFloat(), (cy + radius * Math.sin(angle * it)).toFloat())
            }
            path.close()
            return path
        }
    }
}
