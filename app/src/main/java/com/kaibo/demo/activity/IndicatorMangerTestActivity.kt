package com.kaibo.demo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.kaibo.demo.R
import com.kaibo.demo.adapter.TestAdapter
import com.kaibo.demo.entity.TestEntity
import com.kaibo.indicatrormanagerlib.callback.ItemDragCallBack
import com.kaibo.indicatrormanagerlib.entity.OptionEntity
import kotlinx.android.synthetic.main.activity_indicator_manger_test.*

class IndicatorMangerTestActivity : AppCompatActivity() {

    private val select = mutableListOf("要闻", "体育", "新时代", "汽车", "时尚", "国际", "电影", "财经", "游戏", "科技", "房产", "政务", "图片", "独家",
            "娱乐", "军事", "文化", "视频", "股票", "动漫", "理财", "电竞", "数码", "星座", "教育", "美容", "旅游",
            "重庆", "深圳", "汕头", "东莞", "佛山", "江门", "湛江", "惠州", "中山", "揭阳", "韶关", "茂名", "肇庆", "梅州", "汕尾", "河源", "云浮", "四川")
            .mapIndexed { index, s ->
                TestEntity("$s$index").apply {
                    isSelect = index % 2 == 0
                } as OptionEntity
            }
            .toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indicator_manger_test)

        Log.d("Indicator---", "创建适配器前  ${select.size}")

        //创建适配器
        val testAdapter = TestAdapter(select, R.layout.option_item, R.layout.decoration_item)

        Log.d("Indicator---", "创建适配器后  ${select.size}")

        val manager = GridLayoutManager(this, 4)

        testAdapter.setSpanSizeLookup { gridLayoutManager, position ->
            if (select[position].isDecoration) {
                gridLayoutManager.spanCount
            } else {
                1
            }
        }

        //设置布局管理器
        recyclerView.layoutManager = manager

        //设置Adapter
        recyclerView.adapter = testAdapter

        select.forEach {
            //            Log.d("Indicator---", "创建适配器后  ${it.spanSize}")
        }

        val itemDragCallBack = ItemDragCallBack(testAdapter)
        val itemTouchHelper = ItemTouchHelper(itemDragCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
