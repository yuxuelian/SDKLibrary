package com.kaibo.weightlib

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import java.lang.IllegalStateException

/**
 * @author Administrator
 * @date 2018/3/22 21:57
 * GitHub：
 * email：
 * description：抽屉布局   上拉查看更多
 */
class BottomSheetLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "BottomSheetLayout"

        /**
         * 默认的展开关闭动画执行时间
         */
        private const val DEFAULT_ANIM_DURATION = 400L

        /**
         * 临界打开滑动速率  速率比这个小则直接关闭   不判断滑动距离
         */
        private const val LIMIT_OPEN_VAL = -4000F

        /**
         * 临界关闭滑动速率  速率比这个大则直接打开   不判断滑动距离
         */
        private const val LIMIT_CLOSE_VAL = -LIMIT_OPEN_VAL
    }

    private lateinit var topView: View
    private lateinit var bottomView: View

    private val viewDragHelper = ViewDragHelper.create(this, 1.0F, MyCollBack())

    /**
     * 展开关闭动画
     */
    private lateinit var anim: ValueAnimator

    /**
     * 动画执行时间
     */
    var duration = DEFAULT_ANIM_DURATION
        set(value) {
            anim.duration = value
            field = value
        }

    /**
     * 当前动画执行进度   如果  完全展开的情况下
     * progress=0  完全展开
     * progress=1  完全折叠
     * 默认完全折叠
     */
    private var progress = 1.0F

    /**
     * 滑动进度监听
     */
    private var onProgressListener: ((progress: Float) -> Unit)? = null

    /**
     * 展开关闭事件监听
     */
    private var onExpandedListener: ((expand: Boolean) -> Unit)? = null

    fun setOnProgressListener(listener: ((progress: Float) -> Unit)?) {
        this.onProgressListener = listener
    }

    fun setOnExpandedListener(listener: ((expand: Boolean) -> Unit)?) {
        this.onExpandedListener = listener
    }

    /**
     * 是否折叠
     *
     * @return
     */
    val isCollapse: Boolean
        get() = progress == 1f

    /**
     * 是否展开
     *
     * @return
     */
    val isExpended: Boolean
        get() = progress == 0f

    /**
     * 折叠起来
     */
    fun collapse() {
        //如果是展开  才执行折叠操作
        if (isExpended) {
            anim.start()
        } else {
            Log.e(TAG, "collapse: 目前不是展开状态   不能进行折叠操作")
        }
    }

    /**
     * 展开
     */
    fun expend() {
        //如果是折叠的   才执行展开操作
        if (isCollapse) {
            //启动展开
            anim.reverse()
            invalidate()
        } else {
            Log.e(TAG, "expend: 目前不是折叠状态   不能进行展开操作")
        }
    }

    /**
     * 交换展开折叠顺序
     */
    fun toggle() {
        when {
            isExpended -> anim.start()
            isCollapse -> anim.reverse()
            else -> Log.e(TAG, "toggle: 滑动动画正在执行中")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = 0
        var height = 0

        when (widthMode) {
            MeasureSpec.AT_MOST ->
                //子元素至多达到指定大小的值
                width = Math.min(this.layoutParams.width, widthSize)
            MeasureSpec.EXACTLY ->
                //父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小
                width = widthSize
            MeasureSpec.UNSPECIFIED -> {
                //父容器没有对我有任何限制
                width = this.layoutParams.width
            }
        }

        when (heightMode) {
            View.MeasureSpec.AT_MOST -> height = Math.min(this.layoutParams.height, heightSize)
            View.MeasureSpec.EXACTLY -> height = heightSize
            View.MeasureSpec.UNSPECIFIED -> height = this.layoutParams.height
        }

        //测量第一个子View的尺寸
        topView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(topView.layoutParams.height, MeasureSpec.EXACTLY))
        //测量第二个子View的尺寸
        bottomView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height - topView.measuredHeight, MeasureSpec.EXACTLY))
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.d(TAG, "onLayout: ------")
        if (changed) {
            topView.layout(0, bottomView.measuredHeight, measuredWidth, measuredHeight)
            bottomView.layout(0, measuredHeight, measuredWidth, measuredHeight + bottomView.measuredHeight)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val childCount = childCount
        if (childCount != 2) {
            throw IllegalStateException("请添加两个子View")
        }

        topView = getChildAt(0)
        bottomView = getChildAt(1)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //view的大小发生改变后    重新复制动画对象
        anim = ValueAnimator.ofInt(0, bottomView.measuredHeight).apply {
            duration = this@BottomSheetLayout.duration
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = 0
            repeatMode = ValueAnimator.RESTART

            addUpdateListener {
                val animatedValue = it.animatedValue as Int

                //修改当前的滑动进度
                progress = animatedValue / bottomView.measuredHeight.toFloat()

                //重新布局
                topView.layout(0,
                        animatedValue,
                        measuredWidth,
                        animatedValue + topView.measuredHeight)

                bottomView.layout(0,
                        animatedValue + topView.measuredHeight,
                        measuredWidth,
                        animatedValue + topView.measuredHeight + bottomView.measuredHeight)
            }

            addListener(object : AnimatorListenerAdapter() {
                /**
                 * 动画结束监听
                 */
                override fun onAnimationEnd(animation: Animator?) {
                    //回调展开监听
                    onExpandedListener?.invoke(isExpended)
                }
            })
        }
    }


    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    /**
     *  invalidate 被调用后这个方法会执行
     */
    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            postInvalidate()
        } else {
            Log.d(TAG, "computeScroll: 滑动结束")
            onProgressListener?.invoke(progress)
        }
    }

    internal inner class MyCollBack : ViewDragHelper.Callback() {

        /**
         * 边界控制
         */
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return Math.max(Math.min(top, bottomView.measuredHeight), 0)
        }

        /**
         * 手释放时回调
         */
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            Log.d(TAG, "onViewReleased: yvel=$yvel")
            //判断当前速率是否大于某个指定的值,大于这个值则直接认为
            when {
                yvel < LIMIT_OPEN_VAL -> {
                    //展开
                    viewDragHelper.settleCapturedViewAt(0, 0)
                }
                yvel > LIMIT_CLOSE_VAL -> {
                    //折叠起来
                    viewDragHelper.settleCapturedViewAt(0, bottomView.measuredHeight)
                }
                else -> {
                    //bottomView 露出一半
                    if (bottomView.top + bottomView.measuredHeight / 2f < measuredHeight) {
                        //展开
                        viewDragHelper.settleCapturedViewAt(0, 0)
                    } else {
                        //折叠起来
                        viewDragHelper.settleCapturedViewAt(0, bottomView.measuredHeight)
                    }
                }
            }
            postInvalidate()
        }

        /**
         * 被捕获view的位置发生改变时回调
         */
        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            bottomView.layout(
                    0,
                    topView.top + topView.measuredHeight,
                    measuredWidth,
                    topView.top + topView.measuredHeight + bottomView.measuredHeight
            )

            progress = topView.top / bottomView.measuredHeight.toFloat()
            //回调进度
            onProgressListener?.invoke(progress)
        }

        /**
         * 纵向的滑动范围
         */
        override fun getViewVerticalDragRange(child: View): Int {
            return measuredHeight - child.measuredHeight
        }

        /**
         * 是否捕获指定的view
         */
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === topView
        }
    }
}
