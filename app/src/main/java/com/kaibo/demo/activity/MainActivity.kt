package com.kaibo.demo.activity

import android.os.Bundle
import com.kaibo.demo.R
import com.kaibo.demo.mvp.contract.MainContract
import com.kaibo.demo.mvp.model.MainModel
import com.kaibo.demo.mvp.presenter.MainPresenter
import com.kaibo.swipemenulib.activity.AbstractSwipeMenuActivity
import com.kaibo.toast.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbstractSwipeMenuActivity<MainPresenter, MainModel>(), MainContract.View {

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getSlideMenuLayout(): Int {
        return R.layout.menu_layout
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
//        text.text = EncryptUtils.getInstance().encrypt("123")
//        text.text = EncryptUtils.getInstance().decrypt("decrypt")
//        println(isRoot())
//        println(isEmulator())
//        println(hasExternalStorage())
//        println(versionCode)
//        println(versionName)
//        println(isGPSEnable)
//        println(isNetworkConnected)
//        println(sha1)

        button.setOnClickListener {
            ToastUtils.showSuccess("123")
//            mPresenter.queryOrderById(123L)
        }

    }
}
