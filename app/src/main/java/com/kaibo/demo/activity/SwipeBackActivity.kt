package com.kaibo.demo.activity

import com.kaibo.demo.mvp.model.MainModel
import com.kaibo.demo.mvp.presenter.MainPresenter
import com.kaibo.demo.mvp.view.MainFragment
import com.kaibo.swipebacklib.activity.BaseSwipeBackActivity

class SwipeBackActivity : BaseSwipeBackActivity<MainModel, MainFragment, MainPresenter>()
