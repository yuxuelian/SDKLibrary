package com.kaibo.indicatrormanagerlib

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kaibo.indicatrormanagerlib.callback.OptionDragCallBack
import com.kaibo.indicatrormanagerlib.callback.OnOptionStateListener
import com.kaibo.indicatrormanagerlib.entity.OptionEntity
import java.util.*

/**
 * @author Administrator
 * @date 2018/4/13 0013 上午 11:23
 * GitHub：
 * email：
 * description：
 */
abstract class BaseIndicatorManagerRvAdapter(private var allOption: MutableList<OptionEntity>,
                                             private val gridLayoutManager: GridLayoutManager,
                                             optionLayoutRes: Int,
                                             decorationRes: Int,
                                             var selectedSize: Int = 0,
                                             var fixSelectSize: Int = 0) :
        BaseMultiItemQuickAdapter<OptionEntity, BaseViewHolder>(allOption) {

    private var onOptionStateListener: OnOptionStateListener? = null

    /**
     * 设置新的Item值
     */
    fun setNewOptions(_allOption: MutableList<OptionEntity>,
                      _selectedSize: Int = 0,
                      _fixSelectSize: Int = 0) {
        checkOptions(_allOption, _selectedSize, _fixSelectSize)
        this.allOption = _allOption
        this.selectedSize = _selectedSize
        this.fixSelectSize = _fixSelectSize
        notifyDataSetChanged()
    }

    /**
     * 检查参数
     */
    private fun checkOptions(_allOption: MutableList<OptionEntity>, _selectedSize: Int, _fixSelectSize: Int) {
        if (_allOption.isEmpty()) {
            throw IllegalArgumentException("allOption is empty")
        }

        if (_selectedSize > _allOption.size) {
            throw IllegalArgumentException("选中的Item数不能大于Item总数")
        }

        if (_fixSelectSize > _selectedSize) {
            throw IllegalArgumentException("固定位置的Item数不能大于选中的Item数")
        }
    }

    /**
     * 设置选项变化监听
     */
    fun setOnItemRangeChangeListener(onOptionStateListener: OnOptionStateListener) {
        this.onOptionStateListener = onOptionStateListener
    }

    init {
        checkOptions(allOption, selectedSize, fixSelectSize)
        allOption.add(selectedSize, object : OptionEntity {
            override fun getItemType() = OptionEntity.TAB_TYPE
        })

        //添加多布局类型
        addItemType(OptionEntity.OPTION_TYPE, optionLayoutRes)
        addItemType(OptionEntity.TAB_TYPE, decorationRes)

        this.setOnItemClickListener { adapter, view, position ->
            if (!recyclerView.itemAnimator.isRunning) {
                when {
                    position < fixSelectSize -> {
                        //固定不动区域  不做任何处理
                    }
                    position < selectedSize -> {
                        //可以删除区域  点击删除
                        optionMove(position, selectedSize)
                        selectedSize--
                        onOptionStateListener?.refreshOptionDecoration()
                        onOptionStateListener?.onSelectedOptionList(allOption.subList(0, selectedSize))
                    }
                    position == selectedSize -> {
                        //固定不动区域  不做任何处理
                    }
                    else -> {
                        //可以添加区域  点击添加
                        optionMove(position, selectedSize)
                        selectedSize++
                        onOptionStateListener?.refreshOptionDecoration()
                        onOptionStateListener?.onSelectedOptionList(allOption.subList(0, selectedSize))
                    }
                }
            }
        }
    }


    /**
     * 获取选中的Item
     */
    fun getSelectedItemList() = allOption.subList(0, selectedSize)

    /**
     * 将this绑定到传入的  recyclerView  去
     */
    override fun bindToRecyclerView(recyclerView: RecyclerView) {
        super.bindToRecyclerView(recyclerView)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (allOption[position].itemType == OptionEntity.TAB_TYPE) {
                    gridLayoutManager.spanCount
                } else {
                    1
                }
            }
        }
        recyclerView.layoutManager = gridLayoutManager

        //添加拖拽帮助类
        ItemTouchHelper(OptionDragCallBack(this)).attachToRecyclerView(recyclerView)
    }

    override fun convert(helper: BaseViewHolder, optionEntity: OptionEntity) {
        if (optionEntity.itemType == OptionEntity.TAB_TYPE) {
            convertDecoration(helper.itemView)
        } else {
            convertOptions(helper, optionEntity)
        }
    }

    /**
     * 移动
     */
    fun optionMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(allOption, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(allOption, i, i - 1)
            }
        }
        //刷新   会有默认的移动动画
        notifyItemMoved(fromPosition, toPosition)
    }

    /**
     * 绑定  convertOptions
     * @param helper
     * @param optionEntity
     */
    protected abstract fun convertOptions(helper: BaseViewHolder, optionEntity: OptionEntity?)

    /**
     * 绑定  convertDecoration
     */
    protected abstract fun convertDecoration(decoration: View)

    /**
     * 被拖动的时候这个方法会被回调
     */
    fun onSelectedChanged(helper: BaseViewHolder) {
        onOptionStateListener?.startDrag(helper)
    }

    /**
     * 释放拖动的时候这个方法会被回调
     */
    fun clearView(helper: BaseViewHolder) {

        onOptionStateListener?.endDrag(helper)
    }

}
