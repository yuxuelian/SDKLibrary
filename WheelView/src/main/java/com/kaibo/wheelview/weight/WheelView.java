/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 *
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.kaibo.wheelview.weight;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.kaibo.wheelview.adapter.WheelViewAdapter;
import com.kaibo.wheelview.listener.OnWheelChangedListener;
import com.kaibo.wheelview.listener.OnWheelClickedListener;
import com.kaibo.wheelview.listener.OnWheelScrollListener;
import com.kaibo.wheelview.common.ItemsRange;
import com.kaibo.wheelview.common.WheelRecycle;
import com.kaibo.wheelview.common.WheelScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Numeric wheel view.
 *
 * @author Administrator
 */
public class WheelView extends View {

    /**
     * Top and bottom items offset (to hide that)
     */
    private static final int ITEM_OFFSET_PERCENT = 0;

    /**
     * Left and right padding value
     */
    private static final int PADDING = 16;

    /**
     * Default count of visible items
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    /**
     * Modified by kaibo 2014-11-25
     * private int[] SHADOWS_COLORS = new int[] { 0xFF111111,
     * 0x00AAAAAA, 0x00AAAAAA };
     */
    private int[] shadowsColors = new int[]{0xFFFFFFFF, 0x00FFFFFF, 0x00FFFFFF};

    private int currentItem = 0;

    private int visibleItems = DEF_VISIBLE_ITEMS;

    private int itemHeight = 0;

    private int wheelBackground;

    private GradientDrawable topShadow;
    private GradientDrawable bottomShadow;

    private boolean isDrawShadows = true;

    private WheelScroller scroller;

    private boolean isScrollingPerformed;

    private int scrollingOffset;

    private boolean isCyclic = false;

    private LinearLayout itemsLayout;

    private int firstItem;

    private WheelViewAdapter viewAdapter;

    private WheelRecycle recycle = new WheelRecycle(this);

    private List<OnWheelChangedListener> changingListeners = new ArrayList<>(2);
    private List<OnWheelScrollListener> scrollingListeners = new ArrayList<>(2);
    private List<OnWheelClickedListener> clickingListeners = new ArrayList<>(2);

    @ColorInt
    private int lineColor;

    private DataSetObserver dataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            invalidateWheel(false);
        }

        @Override
        public void onInvalidated() {
            invalidateWheel(true);
        }
    };

    private WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
        @Override
        public void onStarted() {
            isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }

        @Override
        public void onScroll(int distance) {
            doScroll(distance);
            int height = getHeight();
            if (scrollingOffset > height) {
                scrollingOffset = height;
                scroller.stopScrolling();
            } else if (scrollingOffset < -height) {
                scrollingOffset = -height;
                scroller.stopScrolling();
            }
        }

        @Override
        public void onFinished() {
            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd();
                isScrollingPerformed = false;
            }
            scrollingOffset = 0;
            invalidate();
        }

        @Override
        public void onJustify() {
            if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
                scroller.scroll(scrollingOffset, 0);
            }
        }
    };

    /**
     * Constructor
     */
    public WheelView(Context context) {
        this(context, null);
    }

    /**
     * Constructor
     */
    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor
     */
    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
    }

    /**
     * Initializes class data
     */
    private void initData() {
        scroller = new WheelScroller(getContext(), scrollingListener);
    }

    /**
     * Set the the specified scrolling interpolator
     *
     * @param interpolator the interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        scroller.setInterpolator(interpolator);
    }

    /**
     * 获取当前显示的Item个数
     *
     * @return the count of visible items
     */
    public int getVisibleItems() {
        return visibleItems;
    }

    /**
     * 设置最多能显示的Item个数
     *
     * @param count the desired count for visible items
     */
    public void setVisibleItems(int count) {
        visibleItems = count;
    }

    /**
     * 获取适配器
     *
     * @return the view adapter
     */
    public WheelViewAdapter getViewAdapter() {
        return viewAdapter;
    }

    /**
     * 设置适配器
     *
     * @param viewAdapter the view adapter
     */
    public void setViewAdapter(WheelViewAdapter viewAdapter) {
        if (this.viewAdapter != null) {
            this.viewAdapter.unregisterDataSetObserver(dataObserver);
        }

        this.viewAdapter = viewAdapter;

        if (this.viewAdapter != null) {
            this.viewAdapter.registerDataSetObserver(dataObserver);
        }

        invalidateWheel(true);
    }

    /**
     * 添加状态监听
     *
     * @param listener the listener
     */
    public void addChangingListener(OnWheelChangedListener listener) {
        changingListeners.add(listener);
    }

    /**
     * 移除监听
     *
     * @param listener the listener
     */
    public void removeChangingListener(OnWheelChangedListener listener) {
        changingListeners.remove(listener);
    }

    /**
     * 通知更新
     *
     * @param oldValue the old wheel value
     * @param newValue the new wheel value
     */
    protected void notifyChangedListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : changingListeners) {
            listener.onChangedPosition(this, oldValue, newValue);
        }
    }

    /**
     * 添加滚动监听
     *
     * @param listener the listener
     */
    public void addScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.add(listener);
    }

    /**
     * Removes wheel scrolling listener
     *
     * @param listener the listener
     */
    public void removeScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.remove(listener);
    }

    /**
     * Notifies listeners about starting scrolling
     */
    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    /**
     * Adds wheel clicking listener
     *
     * @param listener the listener
     */
    public void addClickingListener(OnWheelClickedListener listener) {
        clickingListeners.add(listener);
    }

    /**
     * Removes wheel clicking listener
     *
     * @param listener the listener
     */
    public void removeClickingListener(OnWheelClickedListener listener) {
        clickingListeners.remove(listener);
    }

    /**
     * Notifies listeners about clicking
     */
    protected void notifyClickListenersAboutClick(int item) {
        for (OnWheelClickedListener listener : clickingListeners) {
            listener.onItemClicked(this, item);
        }
    }

    /**
     * Gets current value
     *
     * @return the current value
     */
    public int getCurrentItem() {
        return currentItem;
    }

    /**
     * Sets the current item. Does nothing when index is wrong.
     *
     * @param index    the item index
     * @param animated the animation flag
     */
    public void setCurrentItem(int index, boolean animated) {
        if (viewAdapter == null || viewAdapter.getCount() == 0) {
            // throw?
            return;
        }

        int itemCount = viewAdapter.getCount();
        if (index < 0 || index >= itemCount) {
            if (isCyclic) {
                while (index < 0) {
                    index += itemCount;
                }
                index %= itemCount;
            } else {
                return;
            }
        }

        if (index != currentItem) {
            if (animated) {
                int itemsToScroll = index - currentItem;
                if (isCyclic) {
                    int scroll = itemCount + Math.min(index, currentItem) - Math.max(index, currentItem);
                    if (scroll < Math.abs(itemsToScroll)) {
                        itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
                    }
                }
                scroll(itemsToScroll, 0);
            } else {
                scrollingOffset = 0;
                int old = currentItem;
                currentItem = index;
                notifyChangedListeners(old, currentItem);
                invalidate();
            }
        }
    }

    /**
     * 暴露方法指定位置
     *
     * @param position the item index
     */
    public void setCurrentItem(int position) {
        setCurrentItem(position, false);
    }

    /**
     * Tests if wheel is cyclic. That means before the 1st item there is shown the last one
     *
     * @return true if wheel is cyclic
     */
    public boolean isCyclic() {
        return isCyclic;
    }

    /**
     * Set wheel cyclic flag
     *
     * @param isCyclic the flag to set
     */
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        invalidateWheel(false);
    }

    /**
     * @return true is shadows are drawn
     */
    public boolean isDrawShadows() {
        return isDrawShadows;
    }

    /**
     * @param drawShadows flag as true or false
     */
    public void setDrawShadows(boolean drawShadows) {
        this.isDrawShadows = drawShadows;
    }

    /**
     * Set the shadow gradient color
     *
     * @param start
     * @param middle
     * @param end
     */
    public void setShadowColor(int start, int middle, int end) {
        shadowsColors = new int[]{start, middle, end};
    }

    /**
     * Sets the drawable for the wheel background
     *
     * @param resource
     */
    public void setWheelBackground(int resource) {
        wheelBackground = resource;
        setBackgroundResource(wheelBackground);
    }

    /**
     * Invalidates wheel
     *
     * @param clearCaches if true then cached views will be clear
     */
    public void invalidateWheel(boolean clearCaches) {
        if (clearCaches) {
            recycle.clearAll();
            if (itemsLayout != null) {
                itemsLayout.removeAllViews();
            }
            scrollingOffset = 0;
        } else if (itemsLayout != null) {
            recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
        }
        invalidate();
    }

    /**
     * @param layout the source layout
     * @return the desired layout height
     */
    private int getDesiredHeight(LinearLayout layout) {
        if (layout != null && layout.getChildAt(0) != null) {
            itemHeight = layout.getChildAt(0).getMeasuredHeight();
        }
        int desired = itemHeight * visibleItems - itemHeight * ITEM_OFFSET_PERCENT / 50;
        return Math.max(desired, getSuggestedMinimumHeight());
    }

    /**
     * Returns height of wheel item
     *
     * @return the item height
     */
    private int getItemHeight() {
        if (itemHeight != 0) {
            return itemHeight;
        }
        if (itemsLayout != null && itemsLayout.getChildAt(0) != null) {
            itemHeight = itemsLayout.getChildAt(0).getHeight();
            return itemHeight;
        }
        return getHeight() / visibleItems;
    }

    /**
     * Calculates control width and creates text layouts
     *
     * @param widthSize the input layout width
     * @param mode      the layout mode
     * @return the calculated control width
     */
    private int calculateLayoutWidth(int widthSize, int mode) {
        if (topShadow == null) {
            topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, shadowsColors);
        }

        if (bottomShadow == null) {
            bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, shadowsColors);
        }

        itemsLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        itemsLayout.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int width = itemsLayout.getMeasuredWidth();
        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width += 2 * PADDING;
            width = Math.max(width, getSuggestedMinimumWidth());
            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize;
            }
        }
        itemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 2 * PADDING, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        buildViewForMeasuring();

        int width = calculateLayoutWidth(widthSize, widthMode);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getDesiredHeight(itemsLayout);
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layout(r - l, b - t);
    }

    /**
     * Sets layouts width and height
     *
     * @param width  the layout width
     * @param height the layout height
     */
    private void layout(int width, int height) {
        int itemsWidth = width - 2 * PADDING;
        itemsLayout.layout(0, 0, itemsWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (viewAdapter != null && viewAdapter.getCount() > 0) {
            updateView();
            drawItems(canvas);
            //画分割线
            drawCenterRect(canvas);
        }

        //画阴影
        if (isDrawShadows) {
            isDrawShadows(canvas);
        }
    }

    /**
     * Draws shadows on top and bottom of control
     *
     * @param canvas the canvas for drawing
     */
    private void isDrawShadows(Canvas canvas) {
        topShadow.setBounds(0, 0, getWidth(), getHeight());
        topShadow.draw(canvas);
        bottomShadow.setBounds(0, 0, getWidth(), getHeight());
        bottomShadow.draw(canvas);
    }

    /**
     * 绘制Items
     *
     * @param canvas the canvas for drawing
     */
    private void drawItems(Canvas canvas) {
        canvas.save();
        int top = (currentItem - firstItem) * getItemHeight() + (getItemHeight() - getHeight()) / 2;
        canvas.translate(PADDING, -top + scrollingOffset);
        itemsLayout.draw(canvas);
        canvas.restore();
    }

    /**
     * Draws rect for current value
     *
     * @param canvas the canvas for drawing
     */
    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = (int) (getItemHeight() / 2 * 1.2);
        Paint paint = new Paint();
        paint.setColor(lineColor);
        // 设置线宽
        paint.setStrokeWidth(1F);
        // 绘制上边直线
        canvas.drawLine(0, center - offset, getWidth(), center - offset, paint);
        // 绘制下边直线
        canvas.drawLine(0, center + offset, getWidth(), center + offset, paint);
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || getViewAdapter() == null) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isScrollingPerformed) {
                    int distance = (int) event.getY() - getHeight() / 2;
                    if (distance > 0) {
                        distance += getItemHeight() / 2;
                    } else {
                        distance -= getItemHeight() / 2;
                    }
                    int items = distance / getItemHeight();
                    if (items != 0 && isValidItemIndex(currentItem + items)) {
                        notifyClickListenersAboutClick(currentItem + items);
                    }
                }
                break;
            default:
                break;
        }

        return scroller.onTouchEvent(event);
    }

    /**
     * 指定滚动动作
     *
     * @param delta the scrolling value
     */
    private void doScroll(int delta) {
        scrollingOffset += delta;
        int itemHeight = getItemHeight();
        int count = scrollingOffset / itemHeight;
        int position = currentItem - count;
        int itemCount = viewAdapter.getCount();
        int fixPos = scrollingOffset % itemHeight;
        if (Math.abs(fixPos) <= itemHeight / 2) {
            fixPos = 0;
        }
        if (isCyclic && itemCount > 0) {
            if (fixPos > 0) {
                position--;
                count++;
            } else if (fixPos < 0) {
                position++;
                count--;
            }
            while (position < 0) {
                position += itemCount;
            }
            position %= itemCount;
        } else {
            if (position < 0) {
                count = currentItem;
                position = 0;
            } else if (position >= itemCount) {
                count = currentItem - itemCount + 1;
                position = itemCount - 1;
            } else if (position > 0 && fixPos > 0) {
                position--;
                count++;
            } else if (position < itemCount - 1 && fixPos < 0) {
                position++;
                count--;
            }
        }

        int offset = scrollingOffset;
        if (position != currentItem) {
            setCurrentItem(position, false);
        } else {
            invalidate();
        }

        scrollingOffset = offset - count * itemHeight;
        if (scrollingOffset > getHeight()) {
            scrollingOffset = scrollingOffset % getHeight() + getHeight();
        }
    }

    /**
     * Scroll the wheel
     *
     * @param itemsToScroll items to scroll
     * @param time          scrolling duration
     */
    public void scroll(int itemsToScroll, int time) {
        int distance = itemsToScroll * getItemHeight() - scrollingOffset;
        scroller.scroll(distance, time);
    }

    /**
     * 获取 以  currentItem  为中心的上下  Items
     *
     * @return the items range
     */
    private ItemsRange getItemsRange() {
        if (getItemHeight() == 0) {
            return null;
        }

        int first = currentItem;
        int count = 1;

        while (count * getItemHeight() < getHeight()) {
            first--;
            count += 2;
        }

        if (scrollingOffset != 0) {
            if (scrollingOffset > 0) {
                first--;
            }
            count++;
            int emptyItems = scrollingOffset / getItemHeight();
            first -= emptyItems;
            count += Math.asin(emptyItems);
        }
        return new ItemsRange(first, count);
    }

    @ColorInt
    private int wheelViewSelectTextColor = Color.parseColor("#FFAA66");
    @ColorInt
    private int wheelViewNormalTextColor = Color.parseColor("#996633");

    /**
     * @return true if items are rebuilt
     */
    private boolean rebuildItems() {
        boolean updated;
        ItemsRange range = getItemsRange();
        if (itemsLayout != null) {
            int first = recycle.recycleItems(itemsLayout, firstItem, range);

            updated = firstItem != first;

            firstItem = first;
        } else {
            createItemsLayout();
            updated = true;
        }

        if (!updated) {
            updated = firstItem != range.getFirst() || itemsLayout.getChildCount() != range.getCount();
        }

        if (firstItem > range.getFirst() && firstItem <= range.getLast()) {
            //前面的View不够的情况
            for (int i = firstItem - 1; i >= range.getFirst(); i--) {
                if (!addViewItem(i, true)) {
                    break;
                }
                firstItem = i;
            }
        } else {
            firstItem = range.getFirst();
        }
        int first = firstItem;
        for (int i = itemsLayout.getChildCount(); i < range.getCount(); i++) {
            if (!addViewItem(firstItem + i, false) && itemsLayout.getChildCount() == 0) {
                first++;
            }
        }
        firstItem = first;
        notifyChildView();
        return updated;
    }

    /**
     * 通知子View更新样式
     */
    private void notifyChildView() {
        if (viewAdapter != null) {
            int childCount = itemsLayout.getChildCount();
            List<View> allChildView = new ArrayList<>(childCount);
            for (int i = 0; i < childCount; i++) {
                allChildView.add(itemsLayout.getChildAt(i));
            }
            viewAdapter.updateView(allChildView, currentItem - firstItem);
        }
    }

    /**
     * Updates view. Rebuilds items and label if necessary, recalculate items sizes.
     */
    private void updateView() {
        if (rebuildItems()) {
            calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
            layout(getWidth(), getHeight());
        }
    }

    /**
     * Creates item layouts if necessary
     */
    private void createItemsLayout() {
        if (itemsLayout == null) {
            itemsLayout = new LinearLayout(getContext());
            itemsLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    /**
     * Builds view for measuring
     */
    private void buildViewForMeasuring() {
        if (itemsLayout != null) {
            recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
        } else {
            createItemsLayout();
        }

        int addItems = visibleItems / 2;
        for (int i = currentItem + addItems; i >= currentItem - addItems; i--) {
            if (addViewItem(i, true)) {
                firstItem = i;
            }
        }
    }

    /**
     * Adds view for item to items layout
     *
     * @param index the item index
     * @param first the flag indicates if view should be first
     * @return true if corresponding item exists and is added
     */
    private boolean addViewItem(int index, boolean first) {
        View view = getItemView(index);
        if (view != null) {
            if (first) {
                itemsLayout.addView(view, 0);
            } else {
                itemsLayout.addView(view);
            }
            return true;
        }
        return false;
    }

    /**
     * Checks whether intem index is valid
     *
     * @param index the item index
     * @return true if item index is not out of bounds or the wheel is cyclic
     */
    private boolean isValidItemIndex(int index) {
        return viewAdapter != null && viewAdapter.getCount() > 0 && (isCyclic || index >= 0 && index < viewAdapter.getCount());
    }

    /**
     * Returns view for specified item
     *
     * @param index the item index
     * @return item view or empty view if index is out of bounds
     */
    private View getItemView(int index) {
        if (viewAdapter == null || viewAdapter.getCount() == 0) {
            return null;
        }
        int count = viewAdapter.getCount();
        if (!isValidItemIndex(index)) {
            return viewAdapter.getEmptyItem(recycle.getEmptyItem(), itemsLayout);
        } else {
            while (index < 0) {
                index = count + index;
            }
        }
        index %= count;
        return viewAdapter.getItem(index, recycle.getItem(), itemsLayout);
    }

    /**
     * Stops scrolling
     */
    public void stopScrolling() {
        scroller.stopScrolling();
    }
}
