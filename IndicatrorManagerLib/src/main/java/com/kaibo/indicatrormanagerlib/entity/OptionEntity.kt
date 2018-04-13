package com.kaibo.indicatrormanagerlib.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author Administrator
 * @date 2018/4/13 0013 下午 1:52
 * GitHub：
 * email：
 * description：
 */
interface OptionEntity : MultiItemEntity {

    companion object {
        val OPTION_TYPE = 0
        val TAB_TYPE = 1
    }

    /**
     * 是否选中  只有被选中的项才能被拖动
     */
    var isSelect: Boolean

    /**
     * 是否是分割View
     */
    val isDecoration: Boolean
}
