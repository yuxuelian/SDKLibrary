package com.kaibo.demo.activity

import android.os.Bundle
import com.kaibo.core.util.sign
import com.kaibo.demo.R
import com.kaibo.demo.mvp.contract.MainContract
import com.kaibo.demo.mvp.model.MainModel
import com.kaibo.demo.mvp.presenter.MainPresenter
import com.kaibo.ndklib.encrypt.EncryptUtils
import com.kaibo.swipemenulib.activity.BaseSwipeMenuActivity
import com.kaibo.core.toast.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.menu_layout.*

class MainActivity : BaseSwipeMenuActivity<MainPresenter, MainModel>(), MainContract.IView {

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getSlideMenuLayout(): Int {
        return R.layout.menu_layout
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        text.text = EncryptUtils.getInstance().encrypt("123")
        text.text = EncryptUtils.getInstance().decrypt("decrypt")
//        println(isRoot())
//        println(isEmulator())
//        println(hasExternalStorage())
//        println(versionCode)
//        println(versionName)
//        println(isGPSEnable)
//        println(isNetworkConnected)
//        println(sha1)

//        MD5:  BF:4F:43:B0:43:93:1A:54:59:A8:1B:B3:C6:51:52:48
//        SHA1: 70:F5:EA:30:D9:BB:CB:0E:5E:C9:5A:D7:90:0B:31:29:71:8F:09:D9

        button.setOnClickListener {
            println(sign("SHA1"))
            ToastUtils.showSuccess("123")
//            mPresenter.queryOrderById(123L)
        }

    }
}
