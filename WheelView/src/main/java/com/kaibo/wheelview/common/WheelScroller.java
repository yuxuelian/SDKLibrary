package com.kaibo.wheelview.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author:Administrator
 */
public class WheelScroller {

    public interface ScrollingListener {
        void onScroll(int distance);

        void onStarted();

        void onFinished();

        void onJustify();
    }

    public static final int MIN_DELTA_FOR_SCROLLING = 1;

    private static final int SCROLLING_DURATION = 300;
    private static final int MESSAGE_SCROLL = 0;
    private static final int MESSAGE_JUSTIFY = 1;

    private ScrollingListener listener;

    private Context context;

    private GestureDetector gestureDetector;

    private Scroller scroller;

    private int lastScrollY;

    private float lastTouchedY;

    private boolean isScrollingPerformed;

    public WheelScroller(Context context, ScrollingListener listener) {
        SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                lastScrollY = 0;
                final int maxY = 0x7FFFFFFF;
                final int minY = -maxY;
                scroller.fling(0, lastScrollY, 0, (int) -velocityY, 0, 0, minY, maxY);
                setNextMessage(MESSAGE_SCROLL);
                return true;
            }
        };
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);

        scroller = new Scroller(context, new AccelerateDecelerateInterpolator());

        this.listener = listener;
        this.context = context;
    }

    public void setInterpolator(Interpolator interpolator) {
        scroller.forceFinished(true);
        scroller = new Scroller(context, interpolator);
    }

    public void scroll(int distance, int time) {
        scroller.forceFinished(true);
        lastScrollY = 0;
        scroller.startScroll(0, 0, 0, distance, time != 0 ? time : SCROLLING_DURATION);
        setNextMessage(MESSAGE_SCROLL);
        startScrolling();
    }

    public void stopScrolling() {
        scroller.forceFinished(true);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchedY = event.getY();
                scroller.forceFinished(true);
                clearMessages();
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceY = (int) (event.getY() - lastTouchedY);
                if (distanceY != 0) {
                    startScrolling();
                    listener.onScroll(distanceY);
                    lastTouchedY = event.getY();
                }
                break;
            default:
                break;
        }

        if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
            justify();
        }

        return true;
    }

    private void setNextMessage(int message) {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    private void clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    /**
     * 滑动过度回弹处理
     */
    private Handler animationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            scroller.computeScrollOffset();
            int currY = scroller.getCurrY();
            int delta = lastScrollY - currY;
            lastScrollY = currY;
            if (delta != 0) {
                listener.onScroll(delta);
            }

            if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                scroller.forceFinished(true);
            }

            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == MESSAGE_SCROLL) {
                justify();
            } else {
                finishScrolling();
            }
        }
    };

    private void justify() {
        listener.onJustify();
        setNextMessage(MESSAGE_JUSTIFY);
    }

    private void startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            listener.onStarted();
        }
    }

    void finishScrolling() {
        if (isScrollingPerformed) {
            listener.onFinished();
            isScrollingPerformed = false;
        }
    }
}
