package com.kaibo.demo.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.kaibo.base.fragment.base.BaseFragment
import com.kaibo.demo.R
import com.kaibo.demo.mvp.model.MainModel
import com.kaibo.demo.mvp.presenter.MainPresenter
import com.kaibo.demo.mvp.view.MainFragment
import com.kaibo.swipemenulib.activity.BaseSwipeMenuActivity

class MainActivity : BaseSwipeMenuActivity<MainModel, MainFragment, MainPresenter>() {

    override fun getSlideMenuFragment(): Fragment {
        return SlideMenuFragment()
    }



    class SlideMenuFragment : BaseFragment() {
        override fun initViewCreated(savedInstanceState: Bundle?) {

        }

        override fun getLayoutRes() = R.layout.fragment_slide_menu
    }
}
