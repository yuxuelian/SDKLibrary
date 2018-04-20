package com.kaibo.indicatrormanagerlib.callback

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.chad.library.adapter.base.BaseViewHolder
import com.kaibo.indicatrormanagerlib.BaseIndicatorManagerRvAdapter

/**
 * @author Administrator
 * @date 2018/4/13 0013 下午 5:12
 * GitHub：
 * email：
 * description：
 */
class OptionDragCallBack(private val mAdapter: BaseIndicatorManagerRvAdapter) : ItemTouchHelper.Callback() {

    /**
     * 绘制虚线的画笔
     */
    private val paint = Paint().apply {
        this.color = Color.GRAY
        this.isAntiAlias = true
        this.strokeWidth = 1f
        this.style = Paint.Style.STROKE
        val pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 5f)    //虚线
        this.pathEffect = pathEffect
    }

    /**
     * 虚线框的内边距
     */
    var mPadding = 2

    /**
     * 这个是用来限制哪些Item能拖动
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        //固定位置及tab下面的channel不能拖动
        if (viewHolder.layoutPosition < mAdapter.fixSelectSize || viewHolder.layoutPosition > mAdapter.selectedSize - 1) {
            return 0
        }

        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * 这个方法是用来限制哪些Item能移动
     */
    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        //拖动的position
//        val fromPosition = viewHolder.adapterPosition
        //释放的position
        val toPosition = target.adapterPosition

        //固定位置及tab下面的channel不能拖动  还有固定位置的
        //mAdapter.selectedSize - 1  因为Tab占一个位置所以要减一
        if (toPosition < mAdapter.fixSelectSize || toPosition > mAdapter.selectedSize - 1) {
            return false
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
    }

    //长按时调用
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            //长按时调用
            mAdapter.onSelectedChanged(viewHolder as BaseViewHolder)
        }
    }

    override fun onChildDrawOver(canvas: Canvas,
                                 recyclerView: RecyclerView,
                                 viewHolder: RecyclerView.ViewHolder,
                                 dX: Float,
                                 dY: Float,
                                 actionState: Int,
                                 isCurrentlyActive: Boolean) {
        super.onChildDrawOver(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (dX != 0f && dY != 0f || isCurrentlyActive) {
            //长按拖拽时底部绘制一个虚线矩形
            canvas.drawRect((viewHolder.itemView.left - mPadding).toFloat(),
                    (viewHolder.itemView.top - mPadding).toFloat(),
                    (viewHolder.itemView.right + mPadding).toFloat(),
                    (viewHolder.itemView.bottom + mPadding).toFloat(),
                    paint)
        }
    }

    override fun clearView(recyclerView: RecyclerView,
                           viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        mAdapter.clearView(viewHolder as BaseViewHolder)
    }

    /**
     * 移动结束会回调这个方法
     */
    override fun onMoved(recyclerView: RecyclerView,
                         viewHolder: RecyclerView.ViewHolder,
                         fromPos: Int,
                         target: RecyclerView.ViewHolder,
                         toPos: Int,
                         x: Int,
                         y: Int) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        mAdapter.optionMove(fromPos, toPos)
    }
}