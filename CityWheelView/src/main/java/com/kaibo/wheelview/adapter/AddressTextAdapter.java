package com.kaibo.wheelview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaibo.wheelview.R;
import com.kaibo.wheelview.data.entity.BaseAddressBean;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * @author Administrator
 * @date 2018/4/24 23:03
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */
public class AddressTextAdapter<T extends BaseAddressBean> extends AbstractWheelAdapter {

    private List<T> list;
    private final LayoutInflater inflater;

    public AddressTextAdapter(Context context, List<T> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setNewData(@NonNull List<T> list) {
        this.list = list;
        notifyDataChanged();
    }

    @NonNull
    public T getData(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getItem(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_birth_year, parent, false);
        }
        ((TextView) convertView).setText(list.get(position).getName());
        return convertView;
    }
}