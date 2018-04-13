package com.kaibo.demo.entity

import com.kaibo.indicatrormanagerlib.entity.OptionEntity

/**
 * @author Administrator
 * @date 2018/4/13 0013 下午 3:42
 * GitHub：
 * email：
 * description：
 */
class TestEntity(var str: String) : OptionEntity {

    /**
     * 是否选中变量
     */
    private var _isSelect = false


    override var isSelect: Boolean
        get() = _isSelect
        set(value) {
            this._isSelect = value
        }

    override val isDecoration: Boolean
        get() = false

    override fun getItemType() = OptionEntity.OPTION_TYPE
}
