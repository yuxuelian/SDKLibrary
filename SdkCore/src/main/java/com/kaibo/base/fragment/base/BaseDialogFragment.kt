package com.kaibo.base.fragment.base

import android.os.Bundle
import android.support.v4.app.DialogFragment
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

abstract class BaseDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container, false)

    }

    abstract fun getLayoutRes(): Int
}