package com.kaibo.wheelview.listener;

import android.view.View;

import com.kaibo.wheelview.weight.WheelView;

/**
 * @author Administrator
 */
public interface OnWheelChangedListener {
    void onChangedPosition(WheelView wheel, int lastPosition, int position);

    void onChangedView(WheelView wheel, View lastView, View view);
}
