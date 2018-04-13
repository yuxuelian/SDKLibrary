package com.kaibo.demo.adapter

import android.util.Log
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.kaibo.demo.entity.TestEntity
import com.kaibo.indicatrormanagerlib.BaseIndicatrorManagerRvAdapter
import com.kaibo.indicatrormanagerlib.entity.OptionEntity
import kotlinx.android.synthetic.main.option_item.view.*

/**
 * @author Administrator
 * @date 2018/4/13 0013 下午 3:49
 * GitHub：
 * email：
 * description：
 */


class TestAdapter(allOption: MutableList<OptionEntity>, optionLayoutRes: Int, decorationRes: Int) : BaseIndicatrorManagerRvAdapter(allOption, optionLayoutRes, decorationRes) {

    override fun convertOptions(helper: BaseViewHolder, item: OptionEntity?) {
        helper.itemView.itemOptionBtn.text = (item as? TestEntity)?.str ?: ""
    }

    override fun convertDecoration(decoration: View) {
        Log.d("TestAdapter","调用convertDecoration")
    }

}