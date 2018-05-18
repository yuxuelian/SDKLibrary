package com.kaibo.common.fragment.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author Administrator
 * @date 2018/4/2 0002 上午 10:33
 * GitHub：
 * email：
 * description：
 */

abstract class BaseFragment : Fragment() {

    protected lateinit var mAttachActivity: FragmentActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mAttachActivity = context as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container, false)
//        return container?.inflate(getLayoutRes())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewCreated(savedInstanceState)
    }

    abstract fun getLayoutRes(): Int

    abstract fun initViewCreated(savedInstanceState: Bundle?)
}