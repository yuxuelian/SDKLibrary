/*
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

package com.kaibo.wheelview.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public abstract class BaseWheelAdapter implements WheelViewAdapter {

    private List<DataSetObserver> dataSetObservers;

    @Override
    public void updateView(List<View> allChildView, int selectViewPosition) {

    }

    @Override
    public View getEmptyItem(View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        if (dataSetObservers == null) {
            dataSetObservers = new ArrayList<>();
        }
        dataSetObservers.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (dataSetObservers != null) {
            dataSetObservers.remove(observer);
        }
    }

    /**
     * Notifies observers about data changing
     */
    protected void notifyDataChanged() {
        if (dataSetObservers != null) {
            for (DataSetObserver observer : dataSetObservers) {
                observer.onChanged();
            }
        }
    }

    /**
     * Notifies observers about invalidating data
     */
    protected void notifyDataInvalidated() {
        if (dataSetObservers != null) {
            for (DataSetObserver observer : dataSetObservers) {
                observer.onInvalidated();
            }
        }
    }
}
