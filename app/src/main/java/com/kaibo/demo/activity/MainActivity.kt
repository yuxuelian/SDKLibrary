package com.kaibo.demo.activity

import com.kaibo.base.activity.BaseMvpActivity
import com.kaibo.demo.mvp.model.MainModel
import com.kaibo.demo.mvp.presenter.MainPresenter
import com.kaibo.demo.mvp.view.MainFragment

class MainActivity : BaseMvpActivity<MainModel, MainFragment, MainPresenter>()
