package com.kaibo.core.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.kaibo.core.R
import com.kaibo.core.fragment.base.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_msg.view.*
import org.jetbrains.anko.support.v4.dip

/**
 * @author kaibo
 * @date 2018/6/28 9:38
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */


class MsgDialog : BaseDialogFragment() {

    override fun getLayoutRes() = R.layout.dialog_msg

    var cancelListener: ((View) -> Unit)? = null
    var confirmListener: ((View) -> Unit)? = null
    var msgText: String = "提示消息"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.msg_text.text = msgText
        view.cancel_btn.setOnClickListener {
            cancelListener?.invoke(it)
            dismiss()
        }
        view.confirm_btn.setOnClickListener {
            confirmListener?.invoke(it)
            dismiss()
        }

        dialog.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, R.color.transparent)))
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(dip(280), dip(160))
    }

}