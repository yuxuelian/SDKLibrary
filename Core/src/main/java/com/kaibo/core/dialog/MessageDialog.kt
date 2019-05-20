package com.kaibo.core.dialog

import android.os.Bundle
import androidx.fragment.app.BaseDialogFragment
import com.kaibo.core.R
import com.kaibo.core.util.bindLifecycle
import com.kaibo.core.util.dip
import com.kaibo.core.util.easyClick
import kotlinx.android.synthetic.main.dialog_msg.*

/**
 * @author kaibo
 * @date 2018/6/28 9:38
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */


class MessageDialog : BaseDialogFragment() {

    override fun getLayoutRes() = R.layout.dialog_msg

    var cancelListener: (() -> Unit)? = null
    var confirmListener: (() -> Unit)? = null
    var msgText: String = "提示消息"

    override fun initViewCreated(savedInstanceState: Bundle?) {
        super.initViewCreated(savedInstanceState)
        msg_text.text = msgText

        cancel_btn.easyClick(bindLifecycle()).subscribe {
            cancelListener?.invoke()
            dismiss()
        }

        confirm_btn.easyClick(bindLifecycle()).subscribe {
            confirmListener?.invoke()
            dismiss()
        }
    }

    override fun getSize() = Pair(dip(280), dip(160))
}