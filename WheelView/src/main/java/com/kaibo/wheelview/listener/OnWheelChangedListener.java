package com.kaibo.wheelview.listener;

import com.kaibo.wheelview.weight.WheelView;

/**
 * @author Administrator
 */
public interface OnWheelChangedListener {
    void onChangedPosition(WheelView wheel, int lastPosition, int position);
}
