package com.kaibo.indicatrormanagerlib.callback

import com.chad.library.adapter.base.BaseViewHolder
import com.kaibo.indicatrormanagerlib.entity.OptionEntity

/**
 * @author:Administrator
 * @date:2018/4/14 0014 上午 10:26
 * GitHub:
 * email:
 * description:
 */
interface OnOptionStateListener {
    /**
     * 如果设置了itemDecoration，必须调用recyclerView.invalidateItemDecorations(),否则间距会不对
     */
    fun refreshOptionDecoration()

    fun onSelectedOptionList(selectOptions: List<OptionEntity>)

    fun startDrag(baseViewHolder: BaseViewHolder)

    fun endDrag(baseViewHolder: BaseViewHolder)
}