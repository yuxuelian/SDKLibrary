package com.kaibo.indicatrormanagerlib.entity

/**
 * @author Administrator
 * @date 2018/4/13 0013 下午 3:27
 * GitHub：
 * email：
 * description：分割线   item  实体
 */
class DecorationEntity : OptionEntity {

    override var isSelect: Boolean
        get() = false
        set(value) {}

    override val isDecoration: Boolean
        get() = true

    override fun getItemType() = OptionEntity.TAB_TYPE
}
