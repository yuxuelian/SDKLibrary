package com.kaibo.wheelview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaibo.wheelview.R;
import com.kaibo.wheelview.data.entity.BaseAddressBean;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * @author Administrator
 * @date 2018/4/24 23:03
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：城市选择适配器
 */

public class SelectCityWheelAdapter<T extends BaseAddressBean> extends BaseWheelAdapter {

    private List<T> list;
    private final LayoutInflater inflater;

    public SelectCityWheelAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setNewData(@NonNull List<T> list) {
        this.list = list;
        notifyDataChanged();
    }

    @NonNull
    public T getData(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public View getItem(int position, View convertView, ViewGroup parent) {

        if (list == null) {
            return null;
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_wheel_view, parent, false);
        }

        TextView itemView = (TextView) convertView;
        itemView.setText(list.get(position).getName());
        return itemView;
    }
}
