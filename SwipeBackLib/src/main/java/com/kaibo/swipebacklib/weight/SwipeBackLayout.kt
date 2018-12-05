package com.kaibo.swipebacklib.weight

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kaibo.swipebacklib.R
import java.util.*

class SwipeBackLayout @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyle: Int = R.attr.SwipeBackLayoutStyle) : FrameLayout(context, attrs) {

    private var mEdgeFlag: Int = 0

    private var mScrollThreshold = DEFAULT_SCROLL_THRESHOLD

    private lateinit var mActivity: Activity

    private lateinit var mContentView: View

    private val mDragHelper: ViewDragHelper

    private var mScrollPercent: Float = 0f

    private var mContentLeft: Int = 0

    private var mContentTop: Int = 0

    private val mListeners: MutableList<SwipeListener> by lazy {
        ArrayList<SwipeListener>()
    }

    private lateinit var mShadowLeft: Drawable

    private lateinit var mShadowRight: Drawable

    private lateinit var mShadowBottom: Drawable

    private var mScrimOpacity: Float = 0.toFloat()

    private var mScrimColor = DEFAULT_SCRIM_COLOR

    private var mInLayout: Boolean = false

    private val mTmpRect = Rect()

    private var mTrackingEdge: Int = 0

    init {
        mDragHelper = ViewDragHelper.create(this, ViewDragCallback())
        val a = context.obtainStyledAttributes(attrs, R.styleable.SwipeBackLayout, defStyle, R.style.SwipeBackLayout)

        // 设置响应那种边缘滑动
        setEdgeTrackingEnabled(EDGE_FLAGS[a.getInt(R.styleable.SwipeBackLayout_edge_flag, 0)])

        val shadowLeft = a.getResourceId(R.styleable.SwipeBackLayout_shadow_left, R.drawable.shadow_left)
        val shadowRight = a.getResourceId(R.styleable.SwipeBackLayout_shadow_right, R.drawable.shadow_right)
        val shadowBottom = a.getResourceId(R.styleable.SwipeBackLayout_shadow_bottom, R.drawable.shadow_bottom)
        setShadow(shadowLeft, EDGE_LEFT)
        setShadow(shadowRight, EDGE_RIGHT)
        setShadow(shadowBottom, EDGE_BOTTOM)
        a.recycle()
        val density = resources.displayMetrics.density
        val minVel = MIN_FLING_VELOCITY * density
        mDragHelper.minVelocity = minVel
    }

    /**
     * Set up contentView which will be moved by user gesture
     *
     * @param view
     */
    private fun setContentView(view: View) {
        mContentView = view
    }

    /**
     * Enable edge tracking for the selected edges of the parent com.kaibo.mvp.view. The
     * callback's
     * [ViewDragHelper.Callback.onEdgeTouched]
     * and
     * [ViewDragHelper.Callback.onEdgeDragStarted]
     * methods will only be invoked for edges for which edge tracking has been
     * enabled.
     *
     * @param edgeFlags Combination of edge flags describing the edges to watch
     * @see .EDGE_LEFT
     *
     * @see .EDGE_RIGHT
     *
     * @see .EDGE_BOTTOM
     */
    fun setEdgeTrackingEnabled(edgeFlags: Int) {
        mEdgeFlag = edgeFlags
        mDragHelper.setEdgeTrackingEnabled(mEdgeFlag)
    }

    /**
     * Set a color to use for the scrim that obscures primary content while a
     * drawer is open.
     *
     * @param color Color to use in 0xAARRGGBB format.
     */
    fun setScrimColor(color: Int) {
        mScrimColor = color
        invalidate()
    }

    /**
     * Add a callback to be invoked when a swipe event is sent to this com.kaibo.mvp.view.
     *
     * @param listener the swipe listener to attach to this com.kaibo.mvp.view
     */
    fun addSwipeListener(listener: SwipeListener) {
        mListeners.add(listener)
    }

    /**
     * Removes a listener from the set of listeners
     *
     * @param listener
     */
    fun removeSwipeListener(listener: SwipeListener) {
        mListeners.remove(listener)
    }

    interface SwipeListener {
        /**
         * Invoke when state change
         *
         * @param state         flag to describe scroll state
         * @param scrollPercent scroll percent of this com.kaibo.mvp.view
         * @see .STATE_IDLE
         *
         * @see .STATE_DRAGGING
         *
         * @see .STATE_SETTLING
         */
        fun onScrollStateChange(state: Int, scrollPercent: Float)

        /**
         * Invoke when edge touched
         *
         * @param edgeFlag edge flag describing the edge being touched
         * @see .EDGE_LEFT
         *
         * @see .EDGE_RIGHT
         *
         * @see .EDGE_BOTTOM
         */
        fun onEdgeTouch(edgeFlag: Int)

        /**
         * Invoke when scroll percent over the threshold for the first time
         */
        fun onScrollOverThreshold()
    }

    /**
     * Set scroll threshold, we will close the activity, when scrollPercent over
     * this value
     *
     * @param threshold
     */
    fun setScrollThresHold(threshold: Float) {
        if (threshold >= 1.0f || threshold <= 0) {
            throw IllegalArgumentException("Threshold value should be between 0 and 1.0")
        }
        mScrollThreshold = threshold
    }

    /**
     * Set a drawable used for edge shadow.
     *
     * @param shadow   Drawable to use
     * @param edgeFlag Combination of edge flags describing the edge to set
     * @see .EDGE_LEFT
     *
     * @see .EDGE_RIGHT
     *
     * @see .EDGE_BOTTOM
     */
    fun setShadow(shadow: Drawable, edgeFlag: Int) {
        when {
            edgeFlag and EDGE_LEFT != 0 -> mShadowLeft = shadow
            edgeFlag and EDGE_RIGHT != 0 -> mShadowRight = shadow
            edgeFlag and EDGE_BOTTOM != 0 -> mShadowBottom = shadow
        }
        invalidate()
    }

    /**
     * Set a drawable used for edge shadow.
     *
     * @param resId    Resource of drawable to use
     * @param edgeFlag Combination of edge flags describing the edge to set
     * @see .EDGE_LEFT
     *
     * @see .EDGE_RIGHT
     *
     * @see .EDGE_BOTTOM
     */
    fun setShadow(@DrawableRes resId: Int, edgeFlag: Int) {
        ContextCompat.getDrawable(context, resId)?.let {
            setShadow(it, edgeFlag)
        }
    }

    fun scrollToFinishActivity() {
        val childWidth = mContentView.width
        val childHeight = mContentView.height

        var left = 0
        var top = 0
        when {
            mEdgeFlag and EDGE_LEFT != 0 -> {
                left = childWidth + mShadowLeft.intrinsicWidth + OVERSCROLL_DISTANCE
                mTrackingEdge = EDGE_LEFT
            }
            mEdgeFlag and EDGE_RIGHT != 0 -> {
                left = -childWidth - mShadowRight.intrinsicWidth - OVERSCROLL_DISTANCE
                mTrackingEdge = EDGE_RIGHT
            }
            mEdgeFlag and EDGE_BOTTOM != 0 -> {
                top = -childHeight - mShadowBottom.intrinsicHeight - OVERSCROLL_DISTANCE
                mTrackingEdge = EDGE_BOTTOM
            }
        }

        mDragHelper.smoothSlideViewTo(mContentView, left, top)
        invalidate()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return try {
            mDragHelper.shouldInterceptTouchEvent(event)
        } catch (e: ArrayIndexOutOfBoundsException) {
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragHelper.processTouchEvent(event)
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mInLayout = true
        mContentView.layout(mContentLeft, mContentTop, mContentLeft + mContentView!!.measuredWidth, mContentTop + mContentView!!.measuredHeight)
        mInLayout = false
    }

    override fun requestLayout() {
        if (!mInLayout) {
            super.requestLayout()
        }
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        val drawContent = child === mContentView
        val ret = super.drawChild(canvas, child, drawingTime)
        if (mScrimOpacity > 0 && drawContent && mDragHelper.viewDragState != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child)
            drawScrim(canvas, child)
        }
        return ret
    }

    private fun drawScrim(canvas: Canvas, child: View) {
        val baseAlpha = (mScrimColor and -0x1000000).ushr(24)
        val alpha = (baseAlpha * mScrimOpacity).toInt()
        val color = alpha shl 24 or (mScrimColor and 0XFFFFFF)

        when {
            mTrackingEdge and EDGE_LEFT != 0 -> canvas.clipRect(0, 0, child.left, height)
            mTrackingEdge and EDGE_RIGHT != 0 -> canvas.clipRect(child.right, 0, right, height)
            mTrackingEdge and EDGE_BOTTOM != 0 -> canvas.clipRect(child.left, child.bottom, right, height)
        }
        canvas.drawColor(color)
    }

    private fun drawShadow(canvas: Canvas, child: View) {
        val childRect = mTmpRect
        child.getHitRect(childRect)

        if (mEdgeFlag and EDGE_LEFT != 0) {
            mShadowLeft.setBounds(childRect.left - mShadowLeft.intrinsicWidth,
                    childRect.top,
                    childRect.left,
                    childRect.bottom)
            mShadowLeft.alpha = (mScrimOpacity * FULL_ALPHA).toInt()
            mShadowLeft.draw(canvas)
        }

        if (mEdgeFlag and EDGE_RIGHT != 0) {
            mShadowRight.setBounds(childRect.right,
                    childRect.top,
                    childRect.right + mShadowRight.intrinsicWidth,
                    childRect.bottom)
            mShadowRight.alpha = (mScrimOpacity * FULL_ALPHA).toInt()
            mShadowRight.draw(canvas)
        }

        if (mEdgeFlag and EDGE_BOTTOM != 0) {
            mShadowBottom.setBounds(childRect.left,
                    childRect.bottom,
                    childRect.right,
                    childRect.bottom + mShadowBottom.intrinsicHeight)
            mShadowBottom.alpha = (mScrimOpacity * FULL_ALPHA).toInt()
            mShadowBottom.draw(canvas)
        }
    }

    fun attachToActivity(activity: Activity) {
        mActivity = activity
        val typedArray = activity.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
        val background = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        val decorView = activity.window.decorView as ViewGroup
        val decorChild = decorView.getChildAt(0) as ViewGroup
        decorChild.setBackgroundResource(background)
        decorView.removeView(decorChild)
        addView(decorChild)
        setContentView(decorChild)
        decorView.addView(this)
    }

    override fun computeScroll() {
        mScrimOpacity = 1 - mScrollPercent
        if (mDragHelper.continueSettling(true)) {
            this.postInvalidateOnAnimation()
        }
    }

    private inner class ViewDragCallback : ViewDragHelper.Callback() {
        private var mIsScrollOverValid: Boolean = false

        override fun tryCaptureView(view: View, i: Int): Boolean {
            val ret = mDragHelper.isEdgeTouched(mEdgeFlag, i)
            if (ret) {
                when {
                    mDragHelper.isEdgeTouched(EDGE_LEFT, i) -> mTrackingEdge = EDGE_LEFT
                    mDragHelper.isEdgeTouched(EDGE_RIGHT, i) -> mTrackingEdge = EDGE_RIGHT
                    mDragHelper.isEdgeTouched(EDGE_BOTTOM, i) -> mTrackingEdge = EDGE_BOTTOM
                }
                mListeners.forEach {
                    it.onEdgeTouch(mTrackingEdge)
                }
                mIsScrollOverValid = true
            }
            var directionCheck = false
            if (mEdgeFlag == EDGE_LEFT || mEdgeFlag == EDGE_RIGHT) {
                directionCheck = !mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL, i)
            } else if (mEdgeFlag == EDGE_BOTTOM) {
                directionCheck = !mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_HORIZONTAL, i)
            } else if (mEdgeFlag == EDGE_ALL) {
                directionCheck = true
            }
            return ret and directionCheck
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return mEdgeFlag and (EDGE_LEFT or EDGE_RIGHT)
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return mEdgeFlag and EDGE_BOTTOM
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            when {
                mTrackingEdge and EDGE_LEFT != 0 -> {
                    mScrollPercent = Math.abs(left.toFloat() / (mContentView.width + mShadowLeft.intrinsicWidth))
                }
                mTrackingEdge and EDGE_RIGHT != 0 -> {
                    mScrollPercent = Math.abs(left.toFloat() / (mContentView.width + mShadowRight.intrinsicWidth))
                }
                mTrackingEdge and EDGE_BOTTOM != 0 -> {
                    mScrollPercent = Math.abs(top.toFloat() / (mContentView.height + mShadowBottom.intrinsicHeight))
                }
            }
            mContentLeft = left
            mContentTop = top
            invalidate()
            if (mScrollPercent < mScrollThreshold && !mIsScrollOverValid) {
                mIsScrollOverValid = true
            }
            if (mDragHelper.viewDragState == STATE_DRAGGING && mScrollPercent >= mScrollThreshold && mIsScrollOverValid) {
                mIsScrollOverValid = false
                mListeners.forEach {
                    it.onScrollOverThreshold()
                }
            }

            if (mScrollPercent >= 1) {
                if (!mActivity.isFinishing) {
                    mActivity.finish()
                    mActivity.overridePendingTransition(0, 0)
                }
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val childWidth = releasedChild.width
            val childHeight = releasedChild.height

            var left = 0
            var top = 0
            when {
                mTrackingEdge and EDGE_LEFT != 0 -> {
                    left = if (xvel > 0 || xvel == 0f && mScrollPercent > mScrollThreshold) {
                        childWidth + mShadowLeft.intrinsicWidth + OVERSCROLL_DISTANCE
                    } else {
                        0
                    }
                }
                mTrackingEdge and EDGE_RIGHT != 0 -> {
                    left = if (xvel < 0 || xvel == 0f && mScrollPercent > mScrollThreshold) {
                        -(childWidth + mShadowLeft.intrinsicWidth + OVERSCROLL_DISTANCE)
                    } else {
                        0
                    }
                }
                mTrackingEdge and EDGE_BOTTOM != 0 -> {
                    top = if (yvel < 0 || yvel == 0f && mScrollPercent > mScrollThreshold) {
                        -(childHeight + mShadowBottom.intrinsicHeight + OVERSCROLL_DISTANCE)
                    } else {
                        0
                    }
                }
            }

            mDragHelper.settleCapturedViewAt(left, top)
            invalidate()
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var ret = 0
            if (mTrackingEdge and EDGE_LEFT != 0) {
                ret = Math.min(child.width, Math.max(left, 0))
            } else if (mTrackingEdge and EDGE_RIGHT != 0) {
                ret = Math.min(0, Math.max(left, -child.width))
            }
            return ret
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            var ret = 0
            if (mTrackingEdge and EDGE_BOTTOM != 0) {
                ret = Math.min(0, Math.max(top, -child.height))
            }
            return ret
        }

        override fun onViewDragStateChanged(state: Int) {
            mListeners.forEach {
                it.onScrollStateChange(state, mScrollPercent)
            }
        }
    }

    companion object {

        private const val MIN_FLING_VELOCITY = 400

        private const val DEFAULT_SCRIM_COLOR = -0x67000000

        private const val FULL_ALPHA = 255

        /**
         * 关闭或者复位的阈值
         */
        private const val DEFAULT_SCROLL_THRESHOLD = 0.382f

        private const val OVERSCROLL_DISTANCE = 10

        val EDGE_LEFT = ViewDragHelper.EDGE_LEFT

        val EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT

        val EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM

        val EDGE_ALL = EDGE_LEFT or EDGE_RIGHT or EDGE_BOTTOM

        /**
         * 空闲状态
         */
        val STATE_IDLE = ViewDragHelper.STATE_IDLE

        /**
         * 正在拖拽
         */
        val STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING

        /**
         * 已松手   正在计算最终位置
         */
        val STATE_SETTLING = ViewDragHelper.STATE_SETTLING

        private val EDGE_FLAGS = intArrayOf(EDGE_LEFT, EDGE_RIGHT, EDGE_BOTTOM, EDGE_ALL)
    }
}
