package androidx.fragment.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kaibo.core.toast.showError
import com.kaibo.core.util.bindLifecycle
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @author:Administrator
 * @date:2018/4/2 0002 上午 10:33
 * GitHub:
 * email:
 * description:
 */

abstract class BaseFragment : Fragment() {


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
//        return container?.inflate(getLayoutRes())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewCreated(savedInstanceState)
    }

    protected open fun initViewCreated(savedInstanceState: Bundle?) {

    }

    protected abstract fun getLayoutRes(): Int
}