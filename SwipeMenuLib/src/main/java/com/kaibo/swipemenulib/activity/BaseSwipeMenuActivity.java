package com.kaibo.swipemenulib.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.kaibo.core.activity.SuperActivity;
import com.kaibo.swipemenulib.R;
import com.kaibo.swipemenulib.helper.SwipeActivityHelper;
import com.kaibo.swipemenulib.weight.SwipeMenuLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author:Administrator 继承这个View可以轻松实现侧滑菜单效果
 */
public abstract class BaseSwipeMenuActivity extends SuperActivity {

    private SwipeActivityHelper mHelper;

    @NotNull
    protected SwipeMenuLayout mSwipeMenuLayout;

    /**
     * 返回一个侧滑菜单中填充的Fragment
     *
     * @return
     */
//    @NotNull
//    protected abstract Fragment getSlideMenuFragment();
    @NotNull
    @LayoutRes
    protected abstract int getSlideMenuLayout();


    @Override
    protected void setContentViewBefore(@Nullable Bundle savedInstanceState) {
        //创建SwipeActivityHelper
        this.mHelper = new SwipeActivityHelper(this);

        //创建一个容器
        FrameLayout frameLayout = new FrameLayout(this);
        //给容器指定固定的Id
        frameLayout.setId(R.id.slide_menu_container);
        //给侧滑菜单中添加一个FrameLayout容器
        mHelper.setBehindContentView(frameLayout);
//        //给容器填充一个Fragment
//        this.getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(), this.getSlideMenuFragment()).commit();


        //获取到侧滑布局
        this.mSwipeMenuLayout = mHelper.getSlidingMenu();

        mSwipeMenuLayout.setMenu(getSlideMenuLayout());

        //设置左侧布局偏移量
        mSwipeMenuLayout.setBehindOffsetRes(R.dimen.default_sliding_menu_offset);
        //设置只能从边界滑出菜单
        mSwipeMenuLayout.setTouchModeAbove(SwipeMenuLayout.TOUCH_MODE_MARGIN);
        //设置左侧的阴影宽度
        mSwipeMenuLayout.setShadowWidthRes(R.dimen.default_shadow_width);
        //设置阴影
//        mSwipeMenuLayout.setShadowDrawable();

        //设置菜单宽度
//        mSwipeMenuLayout.setBehindWidthRes(R.dimen.default_sliding_menu_width);

        Resources resources = this.getResources();
        //设置侧滑菜单的宽度
        int widthPixels = resources.getDisplayMetrics().widthPixels;
        mSwipeMenuLayout.setBehindWidth((int) ((double) widthPixels * 0.618D));

        //使用阴影
        mSwipeMenuLayout.setFadeEnabled(true);

        //阴影透明程度
        mSwipeMenuLayout.setFadeDegree(0.8F);

        //设置ActionBar不跟随滑动,这样能实现全屏侧滑
        mHelper.setSlidingActionBarEnabled(false);
    }

    @Override
    protected void initOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate(savedInstanceState);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        T view = super.findViewById(id);
        if (view == null) {
            view = mHelper.findViewById(id);
        }
        return view;
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mHelper.onSaveInstanceState(outState);
    }

    @Override
    public void setContentView(int id) {
        View view = this.getLayoutInflater().inflate(id, null);
        this.setContentView(view);
    }

    @Override
    public void setContentView(@NotNull View v) {
        this.setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(@NotNull View v, @NotNull LayoutParams params) {
        super.setContentView(v, params);
        mHelper.registerAboveContentView(v);
    }

    @Override
    public void onBackPressed() {
        if (mSwipeMenuLayout.isMenuShowing()) {
            mSwipeMenuLayout.showContent();
        } else {
            super.onBackPressed();
        }
    }
}
