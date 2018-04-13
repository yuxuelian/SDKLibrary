package com.kaibo.indicatrormanagerlib.callback

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.kaibo.indicatrormanagerlib.BaseIndicatrorManagerRvAdapter

/**
 * @author Administrator
 * @date 2018/4/13 0013 下午 5:12
 * GitHub：
 * email：
 * description：
 */
class ItemDragCallBack(private val mAdapter: BaseIndicatrorManagerRvAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        //固定位置及tab下面的channel不能拖动
//        if (viewHolder.getLayoutPosition() < mAdapter.getFixSize() + 1 || viewHolder.getLayoutPosition() > mAdapter.getSelectedSize()) {
//            return 0
//        }

        if (!mAdapter.isDragEnable(viewHolder.layoutPosition)) {
            return 0
        }

        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = 0
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    //移动的时候会回调这个方法
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        //拖动的position
        val fromPosition = viewHolder.adapterPosition
        //释放的position
        val toPosition = target.adapterPosition

        //固定位置及tab下面的channel不能拖动
//        if (toPosition < mAdapter.getFixSize() + 1 || toPosition > mAdapter.getSelectedSize()) {
//            return false
//        }

//        mAdapter.itemMove(fromPosition, toPosition)
        return mAdapter.isDragEnable(fromPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
    }

    //长按时调用
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ANIMATION_TYPE_DRAG) {

        }
    }

    override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)

    }

    override fun onMoved(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, fromPos: Int, target: RecyclerView.ViewHolder?, toPos: Int, x: Int, y: Int) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)

    }


}