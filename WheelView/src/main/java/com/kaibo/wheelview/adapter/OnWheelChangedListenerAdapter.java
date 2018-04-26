package com.kaibo.wheelview.adapter;

import android.view.View;

import com.kaibo.wheelview.listener.OnWheelChangedListener;
import com.kaibo.wheelview.weight.WheelView;

/**
 * @author Administrator
 * @date 2018/4/26 0026 下午 4:44
 * @GitHub：https://github.com/yuxuelian
 * @email：
 * @description：
 */

public abstract class OnWheelChangedListenerAdapter implements OnWheelChangedListener {
    @Override
    public void onChangedPosition(WheelView wheel, int lastPosition, int position) {

    }

    @Override
    public void onChangedView(WheelView wheel, View lastView, View view) {

    }
}
