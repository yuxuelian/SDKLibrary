package com.kaibo.wheelview.common;

import android.view.View;
import android.widget.LinearLayout;

import com.kaibo.wheelview.weight.WheelView;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Administrator
 */
public class WheelRecycle {

    private List<View> items;
    private List<View> emptyItems;
    private WheelView wheelView;

    public WheelRecycle(WheelView wheelView) {
        this.wheelView = wheelView;
    }

    /**
     * 移除view
     *
     * @param layout
     * @param firstItem
     * @param range
     * @return
     */
    public int recycleItems(LinearLayout layout, int firstItem, ItemsRange range) {
        int index = firstItem;
        for (int i = 0; i < layout.getChildCount(); ) {
            if (!range.contains(index)) {
                recycleView(layout.getChildAt(i), index);
                layout.removeViewAt(i);
                if (i == 0) {
                    firstItem++;
                }
            } else {
                i++;
            }
            index++;
        }
        return firstItem;
    }

    public View getItem() {
        return getCachedView(items);
    }

    public View getEmptyItem() {
        return getCachedView(emptyItems);
    }

    public void clearAll() {
        if (items != null) {
            items.clear();
        }
        if (emptyItems != null) {
            emptyItems.clear();
        }
    }

    private List<View> addView(View view, List<View> cache) {
        if (cache == null) {
            cache = new LinkedList<>();
        }
        cache.add(view);
        return cache;
    }

    private void recycleView(View view, int index) {
        int count = wheelView.getViewAdapter().getCount();
        boolean b = (index < 0 || index >= count) && !wheelView.isCyclic();
        if (b) {
            emptyItems = addView(view, emptyItems);
        } else {
            while (index < 0) {
                index = count + index;
            }
            items = addView(view, items);
        }
    }

    private View getCachedView(List<View> cache) {
        if (cache != null && cache.size() > 0) {
            View view = cache.get(0);
            cache.remove(0);
            return view;
        }
        return null;
    }
}
