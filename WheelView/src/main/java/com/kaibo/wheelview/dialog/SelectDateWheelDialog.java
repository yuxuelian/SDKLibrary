package com.kaibo.wheelview.dialog;

import android.support.annotation.NonNull;

import com.kaibo.wheelview.adapter.SelectDateWheelAdapter;
import com.kaibo.wheelview.adapter.WheelViewAdapter;
import com.kaibo.wheelview.weight.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2018/4/25 0025 上午 10:05
 * GitHub：
 * email：
 * description：日期选择对话框
 */
public class SelectDateWheelDialog extends BaseWheelDialog {

    public static final long DAY_MILLIS = 24 * 60 * 60 * 1000;
    public static final long HOUR_MILLIS=60*60*1000;
    public static final long MINUTE_MILLIS=60*1000;

    /**
     * 保存时间
     */
    private String date;
    private String hour;
    private String minut;

    /**
     * 三个适配器
     */
    private SelectDateWheelAdapter dateAdapter;
    private SelectDateWheelAdapter hourAdapter;
    private SelectDateWheelAdapter minuteAdapter;

    private List<String> dateData;
    private List<String> hourData;
    private List<String> minuteData;

    public SelectDateWheelDialog(@NonNull DialogStyleConfig dialogStyleConfig, long startTime, long step) {
        super(dialogStyleConfig);

        dateData = new ArrayList<>();
        dateData.add("4月1日");
        dateData.add("4月2日");
        dateData.add("4月3日");
        dateData.add("4月4日");
        dateData.add("4月5日");
        dateData.add("4月6日");
        dateData.add("4月7日");
        dateData.add("4月8日");

        hourData = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourData.add(i + "时");
        }

        minuteData = new ArrayList<>();
        minuteData = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minuteData.add(i + "分");
        }

        dateAdapter.setNewData(dateData);
        hourAdapter.setNewData(hourData);
        minuteAdapter.setNewData(minuteData);
    }

    @NonNull
    @Override
    protected WheelViewAdapter getFirstWheelAdapter() {
        dateAdapter = new SelectDateWheelAdapter(getContext());
        return dateAdapter;
    }

    @NonNull
    @Override
    protected WheelViewAdapter getSecondWheelAdapter() {
        hourAdapter = new SelectDateWheelAdapter(getContext());
        return hourAdapter;
    }

    @NonNull
    @Override
    protected WheelViewAdapter getThirdWheelAdapter() {
        minuteAdapter = new SelectDateWheelAdapter(getContext());
        return minuteAdapter;
    }

    @Override
    protected void firstWheelViewChange(WheelView wheel, int oldValue, int newValue) {

    }

    @Override
    protected void secondWheelViewChange(WheelView wheel, int oldValue, int newValue) {

    }

    @Override
    protected void thirdWheelViewChange(WheelView wheel, int oldValue, int newValue) {

    }
}
