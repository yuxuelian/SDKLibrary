package com.kaibo.indicatrormanagerlib

import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kaibo.indicatrormanagerlib.entity.DecorationEntity
import com.kaibo.indicatrormanagerlib.entity.OptionEntity
import java.lang.IllegalStateException
import java.util.*

/**
 * @author Administrator
 * @date 2018/4/13 0013 上午 11:23
 * GitHub：
 * email：
 * description：
 */
abstract class BaseIndicatrorManagerRvAdapter(private val allOption: MutableList<OptionEntity>,
                                              private val optionLayoutRes: Int,
                                              private val decorationRes: Int) :
        BaseMultiItemQuickAdapter<OptionEntity, BaseViewHolder>(allOption) {

    /**
     * 选中的选项大小
     */
    private var selectedSize = 0

    init {
        if (allOption.isEmpty()) {
            throw IllegalStateException("allOption is empty")
        }

        //将选中的  OptionEntity  顺序移动到最前面
        for ((index, value) in allOption.withIndex()) {
            if (value.isSelect) {
                Collections.swap(allOption, selectedSize++, index)
            }
        }

        val decorationEntity: OptionEntity = DecorationEntity()

        //添加  decorationEntity
        allOption.add(selectedSize, decorationEntity)

        addItemType(OptionEntity.OPTION_TYPE, optionLayoutRes)
        addItemType(OptionEntity.TAB_TYPE, decorationRes)
    }


    override fun convert(helper: BaseViewHolder, item: OptionEntity) {
        if (item.itemType == OptionEntity.TAB_TYPE) {
            convertDecoration(helper.itemView)
        } else {
            convertOptions(helper, item)
        }
    }

    /**
     * 检查指定位置的View是否能被拖动
     */
    fun isDragEnable(position: Int) = allOption[position].isSelect

    /**
     * 绑定  convertOptions
     * @param helper
     * @param item
     */
    protected abstract fun convertOptions(helper: BaseViewHolder, item: OptionEntity?)

    protected abstract fun convertDecoration(decoration: View)

}
