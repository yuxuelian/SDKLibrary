package com.kaibo.swipemenulib.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kaibo.base.activity.BaseMvpActivity
import com.kaibo.base.mvp.model.AbstractModel
import com.kaibo.base.mvp.presenter.AbstractRxPresenter
import com.kaibo.base.mvp.view.AbstractFragment
import com.kaibo.swipemenulib.R
import com.kaibo.swipemenulib.SwipeActivityBase
import com.kaibo.swipemenulib.helper.SwipeActivityHelper
import com.kaibo.swipemenulib.weight.SwipeMenuLayout

/**
 * @author Administrator
 * @date 2018/4/2 0002 上午 10:35
 * GitHub：
 * email：
 * description：需要侧滑的Activity只需要继承这个Activity即可实现侧滑菜单
 */

abstract class BaseSwipeMenuActivity<M : AbstractModel, V : AbstractFragment<*>, P : AbstractRxPresenter<*, *>> :
        BaseMvpActivity<M, V, P>(),
        SwipeActivityBase {

    private lateinit var mHelper: SwipeActivityHelper

    protected lateinit var mSwipeMenuLayout: SwipeMenuLayout

//    abstract fun getSlideMenuLayoutRes(): Int

    abstract fun getSlideMenuFragment(): Fragment

    /**
     * setContentView之前调用
     */
    override fun setContentViewBefore(savedInstanceState: Bundle?) {
        mHelper = SwipeActivityHelper(this)
        mHelper.onCreate(savedInstanceState)
        // 初始化侧滑菜单
        // 先给侧滑菜单设置一个布局,此布局中只有一个FrameLayout,
        // 然后使用FragmentManager将Fragment替换掉此FrameLayout,从而降低耦合度
//        setBehindContentView(R.layout.slide_menu_container_layout)
//        setBehindContentView(R.layout.slide_menu_container_layout)
        val frameLayout = FrameLayout(this).apply {
            id = R.id.slide_menu_container
        }
        setBehindContentView(frameLayout)

        // 为侧滑菜单设置布局,为了降低耦合度,一般不用这个
        //mSlidingMenu.setMenu(getSlideMenuLayoutRes());

        // 左菜单
        supportFragmentManager.beginTransaction().replace(frameLayout.id, getSlideMenuFragment()).commit()

        // 获取SlidingMenu
        mSwipeMenuLayout = mHelper.slidingMenu

        // 设置滑动菜单视图的宽度
        mSwipeMenuLayout.setBehindOffsetRes(R.dimen.default_sliding_menu_offset)

        //默认设置为边界
        mSwipeMenuLayout.touchModeAbove = SwipeMenuLayout.TOUCHMODE_MARGIN

        // 设置阴影图片的宽度
        mSwipeMenuLayout.setShadowWidthRes(R.dimen.default_shadow_width)

        // 设置阴影图片
//        mSlidingMenu.setShadowDrawable(R.drawable.shadow);

        //设置侧滑菜单的宽度
//        mSlidingMenu.setBehindWidthRes(R.dimen.defalut_sliding_menu_width)

        //黄金分割 宽度
        val widthPixels = resources.displayMetrics.widthPixels
        mSwipeMenuLayout.setBehindWidth((widthPixels * 0.618).toInt())

        mSwipeMenuLayout.setFadeEnabled(true)

        // 设置渐入渐出效果的值
        mSwipeMenuLayout.setFadeDegree(0.8f)

        // 设置actionBar能否跟随侧滑栏移动，如果没有，则可以去掉
        //如果这个方法设置成true的话   状态栏的部分不会跟随滑动
        setSlidingActionBarEnabled(false)

        //是否允许侧滑
//        mSlidingMenu.isSlidingEnabled = false
    }

    /**
     * setContentView之后调用
     */
    override fun setContentViewAfter(savedInstanceState: Bundle?) {

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPostCreate(android.os.Bundle)
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper.onPostCreate(savedInstanceState)
    }

    override fun <T : View?> findViewById(id: Int): T {
        return super.findViewById<T>(id) ?: mHelper.findViewById<T>(id)
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mHelper.onSaveInstanceState(outState)
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(int)
     */
    override fun setContentView(id: Int) {
        setContentView(layoutInflater.inflate(id, null))
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(android.view.View)
     */
    override fun setContentView(v: View) {
        setContentView(v, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    override fun setContentView(v: View, params: ViewGroup.LayoutParams) {
        super.setContentView(v, params)
        mHelper.registerAboveContentView(v, params)
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SwipeActivityBase#setBehindContentView(int)
     */
    override fun setBehindContentView(id: Int) {
        setBehindContentView(layoutInflater.inflate(id, null))
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SwipeActivityBase#setBehindContentView(android.view.View)
     */
    override fun setBehindContentView(v: View) {
        setBehindContentView(v, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SwipeActivityBase#setBehindContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    override fun setBehindContentView(v: View, params: ViewGroup.LayoutParams) {
        mHelper.setBehindContentView(v, params)
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SwipeActivityBase#toggle()
     */
    override fun toggle() {
        mHelper.toggle()
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SwipeActivityBase#showAbove()
     */
    override fun showContent() {
        mHelper.showContent()
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SwipeActivityBase#showBehind()
     */
    override fun showMenu() {
        mHelper.showMenu()
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SwipeActivityBase#showSecondaryMenu()
     */
    override fun showSecondaryMenu() {
        mHelper.showSecondaryMenu()
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SwipeActivityBase#setSlidingActionBarEnabled(boolean)
     */
    override fun setSlidingActionBarEnabled(b: Boolean) {
        mHelper.setSlidingActionBarEnabled(b)
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
     */
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val b = mHelper.onKeyUp(keyCode, event)
        return if (b) b else super.onKeyUp(keyCode, event)
    }
}
