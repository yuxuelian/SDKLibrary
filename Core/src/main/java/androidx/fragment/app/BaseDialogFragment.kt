package androidx.fragment.app

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import com.kaibo.core.R
import com.kaibo.core.toast.showError
import com.kaibo.core.util.bindLifecycle
import com.kaibo.core.util.dip
import com.kaibo.core.util.immersive
import com.kaibo.core.util.showMenuKey
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @author:Administrator
 * @date:2018/4/2 0002 上午 10:33
 * GitHub:
 * email:
 * description:
 */

abstract class BaseDialogFragment : DialogFragment() {

    private val rxPermissions by lazy {
        RxPermissions(requireActivity())
    }

    /**
     * 封装一下权限申请后的处理逻辑
     */
    protected fun easyRequestPermission(vararg permissionNames: String, reject: (() -> Unit)? = null, resolve: () -> Unit) {
        // 过滤出需没有授权的权限
        val needRequest = permissionNames.filter { !rxPermissions.isGranted(it) }
        if (needRequest.isNotEmpty()) {
            // 请求权限
            rxPermissions.requestEach(*needRequest.toTypedArray())
                    .`as`(bindLifecycle())
                    .subscribe { permission: Permission ->
                        if (permission.granted) {
                            resolve.invoke()
                        } else {
                            reject?.invoke()
                                    ?: if (!permission.shouldShowRequestPermissionRationale) {
                                        context?.showError("所需权限被拒绝,无法进行相关操作")
                                    } else {
                                        context?.showError("所需权限被永久拒绝,请到安全中心开启")
                                    }
                        }
                    }
        } else {
            resolve.invoke()
        }
    }

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

        dialog.window?.decorView?.setPadding(0, 0, 0, 0)

        // 设置dialog为沉浸式
        val (enableImmersive, isLight) = enableImmersive()
        if (enableImmersive) {
            dialog.window?.immersive(isLight)
        }

        // 显示虚拟菜单按钮
        dialog.window?.showMenuKey()

        // 是否隐藏导航条
        if (isHideStatusBar()) {
            dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        // 是否可以主动隐藏对话框
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // 回调返回按键
                onBackPressed()
            } else {
                false
            }
        }

        // 设置点击外部区域是否消失
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside())

        //设置一个默认的对话框动画,默认是淡入淡出,可重写
        dialog.window?.setWindowAnimations(R.style.dialogShadow)

        //去掉默认的背景
        dialog.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.transparent)))

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewCreated(savedInstanceState)
    }

    protected open fun initViewCreated(savedInstanceState: Bundle?) {

    }

    /**
     * 返回布局文件
     */
    abstract fun getLayoutRes(): Int

    /**
     * 设置对话框的大小
     */
    override fun onResume() {
        super.onResume()
        val size = getSize()
        dialog?.window?.setLayout(size.first, size.second)
    }

    /**
     * 点击外部区域  是否消失
     */
    protected open fun canceledOnTouchOutside() = true

    /**
     * 返回键点击回调
     */
    protected open fun onBackPressed() = false

    /**
     * 返回对话框的大小    Pair<宽,高>
     */
    protected open fun getSize(): Pair<Int, Int> = Pair(dip(280), WindowManager.LayoutParams.WRAP_CONTENT)

    override fun show(manager: FragmentManager, tag: String?) {
        mDismissed = false
        mShownByMe = true
        val ft = manager.beginTransaction()
        val fm = manager.findFragmentByTag(tag)
        if (fm != null) {
            ft.remove(fm)
        }
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }
}