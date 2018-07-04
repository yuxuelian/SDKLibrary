
package com.kaibo.swipebacklib.activity;

import android.os.Bundle;
import android.view.View;

import com.kaibo.mvp.activity.BaseMvpActivity;
import com.kaibo.mvp.model.BaseModel;
import com.kaibo.mvp.presenter.BasePresenter;
import com.kaibo.swipebacklib.helper.SwipeBackActivityHelper;
import com.kaibo.swipebacklib.util.Utils;
import com.kaibo.swipebacklib.weight.SwipeBackLayout;

/**
 * @author:Administrator
 * @date:2018/4/2 0002 上午 10:35
 * GitHub:
 * email:
 * description:继承这个Activity可以轻松实现  侧滑返回
 */

public abstract class BaseSwipeBackActivity<P extends BasePresenter<?, ?>, M extends BaseModel>
        extends BaseMvpActivity<P, M> {

    private SwipeBackActivityHelper mHelper;

    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        this.mSwipeBackLayout = mHelper.getSwipeBackLayout();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public <T extends View> T findViewById(int id) {
        T v = super.findViewById(id);
        if (v == null && mHelper != null) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    @Override
    public void onBackPressed() {
        Utils.convertActivityToTranslucent(this);
        mSwipeBackLayout.scrollToFinishActivity();
    }
}
