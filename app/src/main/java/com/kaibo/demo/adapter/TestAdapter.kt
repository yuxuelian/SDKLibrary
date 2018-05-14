package com.kaibo.demo.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.kaibo.demo.entity.TestEntity
import com.kaibo.indicatrormanagerlib.BaseIndicatorManagerRvAdapter
import com.kaibo.indicatrormanagerlib.entity.OptionEntity
import kotlinx.android.synthetic.main.item_option.view.*

/**
 * @author Administrator
 * @date 2018/4/13 0013 下午 3:49
 * GitHub：
 * email：
 * description：
 */

class TestAdapter(allOption: MutableList<OptionEntity>,
                  gridLayoutManager: GridLayoutManager,
                  @LayoutRes
                  optionLayoutRes: Int,
                  @LayoutRes
                  tabTitleLayoutRes: Int,
                  @LayoutRes
                  tabCenterLayoutRes: Int,
                  selectedSize: Int = 0,
                  fixSelectSize: Int = 0) :
        BaseIndicatorManagerRvAdapter(
                allOption,
                gridLayoutManager,
                optionLayoutRes,
                tabTitleLayoutRes,
                tabCenterLayoutRes,
                selectedSize,
                fixSelectSize) {

    override fun convertTabTitle(titleTabView: View) {
        Log.d("TestAdapter", "调用 convertTabTitle")
    }

    override fun convertOptions(helper: BaseViewHolder, optionEntity: OptionEntity?) {
        helper.itemView.itemOptionBtn.text = (optionEntity as? TestEntity)?.str ?: ""
    }

    override fun convertTabCenter(centerTabView: View) {
        Log.d("TestAdapter", "调用 convertTabCenter")
    }

}