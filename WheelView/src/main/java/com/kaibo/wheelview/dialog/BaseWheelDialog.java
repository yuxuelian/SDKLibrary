package com.kaibo.wheelview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaibo.wheelview.R;
import com.kaibo.wheelview.adapter.WheelViewAdapter;
import com.kaibo.wheelview.adapter.OnWheelChangedListenerAdapter;
import com.kaibo.wheelview.weight.WheelView;

/**
 * @author Administrator
 * @date 2018/4/25 0025 下午 1:03
 * GitHub：
 * email：
 * description：
 */
public abstract class BaseWheelDialog extends Dialog {

    protected LinearLayout rootView;
    protected WheelView thirdWheelView;
    protected WheelView secondWheelView;
    protected WheelView firstWheelView;
    protected TextView leftBtn;
    protected TextView rightBtn;
    protected TextView title;
    protected View lineView;

    protected BaseWheelDialog(DialogStyleConfig styleConfig) {
        this(styleConfig.getContext(), R.style.full_screen_dialog);
        styleConfig.initWheelViewStyle(firstWheelView, secondWheelView, thirdWheelView, lineView);
        styleConfig.initOtherViewStyle(leftBtn, rightBtn, title, rootView);
    }

    private BaseWheelDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initWindow();
        initView();
    }

    /**
     * 初始化window窗口
     */
    private void initWindow() {
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
    }

    /**
     * 初始化窗口中的View
     */
    private void initView() {
        this.setContentView(R.layout.dialog_city_select);

        rootView = (LinearLayout) findViewById(R.id.root_view);
        firstWheelView = (WheelView) findViewById(R.id.wv_first);
        secondWheelView = (WheelView) findViewById(R.id.wv_second);
        thirdWheelView = (WheelView) findViewById(R.id.wv_third);
        rightBtn = (TextView) findViewById(R.id.right_btn);
        leftBtn = (TextView) findViewById(R.id.left_btn);
        title = (TextView) findViewById(R.id.title);
        lineView = findViewById(R.id.line_view);

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View completeBtn) {
                if (rightBtnClick(completeBtn)) {
                    dismiss();
                }
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftBtnClick(rightBtn)) {
                    dismiss();
                }
            }
        });

        firstWheelView.setVisibleItems(5);
        firstWheelView.setViewAdapter(getFirstWheelAdapter());
        firstWheelView.addChangingListener(new OnWheelChangedListenerAdapter() {
            @Override
            public void onChangedPosition(WheelView wheel, int lastPosition, int position) {
                firstWheelViewChange(wheel, lastPosition, position);
            }
        });

        secondWheelView.setVisibleItems(5);
        secondWheelView.setViewAdapter(getSecondWheelAdapter());
        secondWheelView.addChangingListener(new OnWheelChangedListenerAdapter() {
            @Override
            public void onChangedPosition(WheelView wheel, int lastPosition, int position) {
                secondWheelViewChange(wheel, lastPosition, position);
            }
        });

        thirdWheelView.setVisibleItems(5);
        thirdWheelView.setViewAdapter(getThirdWheelAdapter());
        thirdWheelView.addChangingListener(new OnWheelChangedListenerAdapter() {
            @Override
            public void onChangedPosition(WheelView wheel, int lastPosition, int position) {
                thirdWheelViewChange(wheel, lastPosition, position);
            }
        });
    }

    /**
     * 获取第一个WheelView适配器
     *
     * @return
     */
    protected abstract @NonNull
    WheelViewAdapter getFirstWheelAdapter();

    /**
     * 获取第二个WheelView适配器
     *
     * @return
     */
    protected abstract @NonNull
    WheelViewAdapter getSecondWheelAdapter();

    /**
     * 获取第三个WheelView适配器
     *
     * @return
     */
    protected abstract @NonNull
    WheelViewAdapter getThirdWheelAdapter();

    /**
     * 第一个WheelView滚动
     *  @param wheel
     * @param oldValue
     * @param newValue
     */
    protected abstract void firstWheelViewChange(WheelView wheel, int oldValue, int newValue);

    /**
     * 第二个WheelView滚动
     *  @param wheel
     * @param oldValue
     * @param newValue
     */
    protected abstract void secondWheelViewChange(WheelView wheel, int oldValue, int newValue);

    /**
     * 第三个WheelView滚动
     *  @param wheel
     * @param oldValue
     * @param newValue
     */
    protected abstract void thirdWheelViewChange(WheelView wheel, int oldValue, int newValue);

    /**
     * 点击完成按钮后   会回调这个方法
     *
     * @param rightBtn
     * @return
     */
    protected boolean rightBtnClick(View rightBtn) {
        return true;
    }

    /**
     * 点击完成按钮后   会回调这个方法
     *
     * @param leftBtn
     * @return
     */
    protected boolean leftBtnClick(View leftBtn) {
        return true;
    }
}
