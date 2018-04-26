package com.kaibo.wheelview.listener;

import com.kaibo.wheelview.weight.WheelView;

/**
 * @author Administrator
 */
public interface OnWheelScrollListener {
    void onScrollingStarted(WheelView wheel);
    void onScrollingFinished(WheelView wheel);
}
