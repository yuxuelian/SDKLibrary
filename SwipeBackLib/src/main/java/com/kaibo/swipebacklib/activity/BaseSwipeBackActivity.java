
package com.kaibo.swipebacklib.activity;

import android.os.Bundle;
import android.view.View;

import com.kaibo.base.activity.BaseMvpActivity;
import com.kaibo.base.mvp.model.AbstractModel;
import com.kaibo.base.mvp.presenter.AbstractRxPresenter;
import com.kaibo.base.mvp.view.AbstractFragment;
import com.kaibo.swipebacklib.SwipeBackActivityBase;
import com.kaibo.swipebacklib.helper.SwipeBackActivityHelper;
import com.kaibo.swipebacklib.util.Utils;
import com.kaibo.swipebacklib.weight.SwipeBackLayout;

/**
 * @author Administrator
 * @date 2018/4/2 0002 上午 10:35
 * GitHub：
 * email：
 * description：侧滑返回Activity
 */

public abstract class BaseSwipeBackActivity<M extends AbstractModel, V extends AbstractFragment<?>, P extends AbstractRxPresenter<?, ?>>
        extends BaseMvpActivity<M, V, P>
        implements SwipeBackActivityBase {

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
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
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
