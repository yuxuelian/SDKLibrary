package com.kaibo.wheelview.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaibo.wheelview.R;
import com.kaibo.wheelview.adapter.WheelViewAdapter;
import com.kaibo.wheelview.listener.OnWheelScrollListener;
import com.kaibo.wheelview.weight.WheelView;

import java.util.List;

/**
 * @author:Administrator
 * @date:2018/4/26 0026 上午 8:43
 * @GitHub:https://github.com/yuxuelian
 * @email:
 * @description:
 */

public class DialogStyleConfig {

    private final Context context;

    private String titleText = "";
    private float titleTextSize = 20F;

    @ColorInt
    private int titleTextColor = Color.parseColor("#333333");

    private String leftButtonText;
    private float leftTextSize = 16F;

    @ColorInt
    private int leftTextColor = Color.parseColor("#999999");

    private String rightButtonText;
    private float rightTextSize = 16F;

    @ColorInt
    private int rightTextColor = Color.parseColor("#FFAA66");

    @ColorInt
    private int lineColor = Color.parseColor("#FFAA66");

    public DialogStyleConfig setTitleText(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public DialogStyleConfig setTitleTextSize(float titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public DialogStyleConfig setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public DialogStyleConfig setLeftButtonText(String leftButtonText) {
        this.leftButtonText = leftButtonText;
        return this;
    }

    public DialogStyleConfig setLeftTextSize(float leftTextSize) {
        this.leftTextSize = leftTextSize;
        return this;
    }

    public DialogStyleConfig setLeftTextColor(int leftTextColor) {
        this.leftTextColor = leftTextColor;
        return this;
    }

    public DialogStyleConfig setRightButtonText(String rightButtonText) {
        this.rightButtonText = rightButtonText;
        return this;
    }

    public DialogStyleConfig setRightTextSize(float rightTextSize) {
        this.rightTextSize = rightTextSize;
        return this;
    }

    public DialogStyleConfig setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
        return this;
    }

    public DialogStyleConfig setLineColor(int lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public DialogStyleConfig(@NonNull Context context) {
        this.context = context;
        leftButtonText = context.getString(R.string.cancel);
        rightButtonText = context.getString(R.string.complete);
    }

    public void initWheelViewStyle(WheelView firstWheelView, WheelView secondWheelView, WheelView thirdWheelView, View lineView) {
        firstWheelView.setLineColor(lineColor);
        secondWheelView.setLineColor(lineColor);
        thirdWheelView.setLineColor(lineColor);
        lineView.setBackgroundColor(lineColor);

        firstWheelView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

            }
        });
    }

    public void initOtherViewStyle(TextView leftBtn, TextView rightBtn, TextView title, LinearLayout rootView) {
        leftBtn.setTextColor(leftTextColor);
        rightBtn.setTextColor(rightTextColor);
        title.setTextColor(titleTextColor);

        leftBtn.setText(leftButtonText);
        rightBtn.setText(rightButtonText);
        title.setText(titleText);

        leftBtn.setTextSize(leftTextSize);
        rightBtn.setTextSize(rightTextSize);
        title.setTextSize(titleTextSize);
    }
}
