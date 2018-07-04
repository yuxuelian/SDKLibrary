package com.kaibo.demo.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * @author kaibo
 * @date 2018/6/20 12:03
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class LoopPagerAdapter(val context: Context, val data: MutableList<Int>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.setImageResource(data[position])
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as? View)
    }

    override fun isViewFromObject(view: View, any: Any) = view === any

    override fun getCount() = data.size
}