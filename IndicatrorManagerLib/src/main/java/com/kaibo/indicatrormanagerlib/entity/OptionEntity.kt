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

    /**
     * 默认设置ItemType为  OPTION_TYPE
     */
    override fun getItemType() = OPTION_TYPE

    companion object {
        const val OPTION_TYPE = 0
        const val TAB_TITLE_TYPE = 1
        const val TAB_CENTER_TYPE = 2
    }
}
