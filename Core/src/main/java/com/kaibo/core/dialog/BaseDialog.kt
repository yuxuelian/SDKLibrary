package com.kaibo.core.dialog

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.*
import com.kaibo.core.R
import com.kaibo.core.util.bindToAutoDispose
import com.kaibo.core.util.immersive
import com.uber.autodispose.AutoDisposeConverter

/**
 * @author:Administrator
 * @date:2018/4/2 0002 上午 10:33
 * GitHub:
 * email:
 * description:
 */

abstract class BaseDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    /**
     * 是否隐藏状态栏
     */
    protected open fun isHideStatusBar() = false

    /**
     * 是否沉浸式   Pair  的第一个参数表示是否沉浸式
     * 第二个参数    当第一个参数为true的时候  第二个参数才生效
     * 第二个参数表示是否使状态栏的颜色变成黑色
     */
    protected open fun enableImmersive() = Pair(true, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // 设置为无标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置dialog为沉浸式
        val (enableImmersive, isLight) = enableImmersive()
        if (enableImmersive) {
            dialog.window.immersive(isLight)
        }
        // 是否隐藏导航条
        if (isHideStatusBar()) {
            dialog.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //去掉默认的背景
        dialog.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, R.color.transparent)))
        initViewCreated(savedInstanceState)
    }

    protected open fun initViewCreated(savedInstanceState: Bundle?) {
        //设置一个默认的对话框动画,默认是淡入淡出,可重写
        dialog.window.setWindowAnimations(R.style.dialogShadow)
    }

    /**
     * 返回布局文件
     */
    abstract fun getLayoutRes(): Int

    /**
     * 生命周期处理
     */
    protected fun <T> bindLifecycle(): AutoDisposeConverter<T> = bindToAutoDispose(this)

    /**
     * 设置对话框的大小
     */
    override fun onResume() {
        super.onResume()
        val size = getSize()
        dialog.window.setLayout(size.first, size.second)
    }

    /**
     * 返回对话框的大小    Pair<宽,高>
     */
    abstract fun getSize(): Pair<Int, Int>

}