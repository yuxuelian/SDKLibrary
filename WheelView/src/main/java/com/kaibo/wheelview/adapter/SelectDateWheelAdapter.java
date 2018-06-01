package com.kaibo.wheelview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaibo.wheelview.R;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * @author:Administrator
 * @date:2018/4/24 23:03
 * @GitHub:https://github.com/yuxuelian
 * @email:kaibo1hao@gmail.com
 * @description:时间选择适配器
 */
public class SelectDateWheelAdapter extends BaseWheelAdapter {

    private List<String> list;
    private final LayoutInflater inflater;

    private float wheelViewSelectTextSize = 16F;

    private float wheelViewNormalTextSize = 14F;

    @ColorInt
    private int wheelViewSelectTextColor = Color.parseColor("#FFAA66");
    @ColorInt
    private int wheelViewNormalTextColor = Color.parseColor("#996633");

    public SelectDateWheelAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setNewData(@NonNull List<String> list) {
        this.list = list;
        notifyDataChanged();
    }

    @NonNull
    public String getData(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void updateView(List<View> allChildView, int selectViewPosition) {
        setTextViewStyle(allChildView, selectViewPosition);
    }

    private void setTextViewStyle(List<View> allChildView, int selectViewPosition) {
        for (View view : allChildView) {
            TextView textView = (TextView) view;
            textView.setTextColor(wheelViewNormalTextColor);
            textView.setTextSize(wheelViewNormalTextSize);
        }

        if (selectViewPosition >= 0 && selectViewPosition < allChildView.size()) {
            TextView textView = (TextView) allChildView.get(selectViewPosition);
            textView.setTextColor(wheelViewSelectTextColor);
            textView.setTextSize(wheelViewSelectTextSize);
        }
    }

    @Override
    public View getItem(int position, View convertView, ViewGroup parent) {
        if (list == null) {
            return null;
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_wheel_view, parent, false);
        }
        ((TextView) convertView).setText(list.get(position));
        return convertView;
    }
}
