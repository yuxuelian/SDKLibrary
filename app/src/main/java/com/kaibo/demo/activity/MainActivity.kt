package com.kaibo.demo.activity

import android.os.Bundle
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.module.LibraryGlideModule
import com.kaibo.demo.R
import com.kaibo.demo.mvp.model.MainModel
import com.kaibo.demo.mvp.presenter.MainPresenter
import com.kaibo.demo.mvp.view.MainFragment
import com.kaibo.ndklib.encrypt.EncryptUtils
import com.kaibo.swipemenulib.activity.BaseSwipeMenuActivity
import kotlinx.android.synthetic.main.menu_layout.*

class MainActivity : BaseSwipeMenuActivity<MainModel, MainFragment, MainPresenter>() {

    override fun getSlideMenuLayout(): Int {
        return R.layout.menu_layout
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        text.text = EncryptUtils.encrypt("123")

    }
}
