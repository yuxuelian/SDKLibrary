package com.kaibo.wheelview.dialog;

import android.support.annotation.NonNull;
import android.view.View;

import com.kaibo.wheelview.adapter.SelectDateWheelAdapter;
import com.kaibo.wheelview.adapter.WheelViewAdapter;
import com.kaibo.wheelview.weight.WheelView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author:Administrator
 * @date:2018/4/25 0025 上午 10:05
 * GitHub:
 * email:
 * description:日期选择对话框
 */
public class SelectDateWheelDialog extends BaseWheelDialog {

    public static final long DAY_MILLIS = 24 * 60 * 60 * 1000;
    public static final long HOUR_MILLIS = 60 * 60 * 1000;
    public static final long MINUTE_MILLIS = 60 * 1000;

    private static final int HOUR_OF_DAY_COUNT = 24;
    private static final int MINUTE_OF_HOUR_COUNT_1 = 6;

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("00");

    /**
     * 时
     */
    private String hour;

    /**
     * 分
     */
    private String minute;

    /**
     * 三个适配器
     */
    private SelectDateWheelAdapter dateAdapter;
    private SelectDateWheelAdapter hourAdapter;
    private SelectDateWheelAdapter minuteAdapter;


    /**
     * 存储  数据的集合
     */
    private List<String> dateData;
    private List<String> hourData;
    private List<String> minuteData;
    private List<String> hourDataStart;
    private List<String> hourDataEnd;
    private List<String> minuteDataStart;
    private List<String> minuteDataEnd;

    /**
     * 临时变量   记录当前Adapter中设置的数据
     */
    private List<String> tempDateData;
    private List<String> tempHourData;
    private List<String> tempMinuteData;

    /**
     * 时间选择的起始值
     */
    private long startTime;


    /**
     * 用于标记当前天是否进1
     */
    private boolean isCeil = false;

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    private OnDateSelectedListener onDateSelectedListener;

    public interface OnDateSelectedListener {
        /**
         * 回调当前选择的日期
         *
         * @param time
         */
        void onDateSelected(long time);
    }

    public SelectDateWheelDialog(@NonNull DialogStyleConfig dialogStyleConfig, long startTime, int count) {
        super(dialogStyleConfig);
        setCanSelectTime(startTime, count);
    }

    /**
     * @param startTime
     * @param count
     */
    private void setCanSelectTime(long startTime, long count) {
        this.startTime = startTime;

        // 时分的分割点
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        int cutHour = calendar.get(Calendar.HOUR_OF_DAY);
        int cutMinute = calendar.get(Calendar.MINUTE);

        //加载日期数据
        dateData = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINESE);
        isCeil = (cutHour == 23 && cutMinute >= 50);
        for (int i = 0; i < count; i++) {
            dateData.add(dateFormat.format(startTime + (isCeil ? i + 1 : i) * DAY_MILLIS));
        }

        //加载时数据
        hourData = new ArrayList<>();
        for (int i = 0; i < HOUR_OF_DAY_COUNT; i++) {
            hourData.add(NUMBER_FORMAT.format(i) + "时");
        }

        //计算时的上边界
        hourDataStart = hourData.subList((cutHour + 1) % 24, HOUR_OF_DAY_COUNT);
        //计算时的下边界
//        hourDataEnd = hourData.subList(0, (cutHour + 1) % 24 + 1);
        //不考虑下边界
        hourDataEnd = hourData;

        //加载分数据
        minuteData = new ArrayList<>();
        for (int i = 0; i < MINUTE_OF_HOUR_COUNT_1; i++) {
            minuteData.add(NUMBER_FORMAT.format(i * 10) + "分");
        }

        //计算分的上边界
        minuteDataStart = minuteData.subList(((cutMinute + 10) % 60) / 10, MINUTE_OF_HOUR_COUNT_1);
        //计算分的下边界
//        minuteDataEnd = minuteData.subList(0, ((cutMinute + 10) % 60) / 10 + 1);
        //不考虑下边界情况
        minuteDataEnd = minuteData;

        dateAdapter.setNewData(tempDateData = dateData);
        hourAdapter.setNewData(tempHourData = hourDataStart);
        minuteAdapter.setNewData(tempMinuteData = minuteDataStart);
        hour = tempHourData.get(0);
        minute = tempMinuteData.get(0);
    }

    /**
     * 获取当前实际选择的时间点
     *
     * @return
     * @throws ParseException
     */
    public long getCurrentSelectTime() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        long time = dateFormat.parse(dateFormat.format(startTime)).getTime() + (isCeil ? DAY_MILLIS : 0L);
        //日期当前的位置
        time += tempDatePosition * DAY_MILLIS;
        //小时的值
        time += Integer.parseInt(hour.replace("时", "")) * HOUR_MILLIS;
        //分钟的值
        time += Integer.parseInt(minute.replace("分", "")) * MINUTE_MILLIS;
        return time;
    }

    @Override
    protected boolean rightBtnClick(View rightBtn) {
        if (onDateSelectedListener != null) {
            try {
                onDateSelectedListener.onDateSelected(getCurrentSelectTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return super.rightBtnClick(rightBtn);
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

    private int tempDatePosition;

    @Override
    protected void firstWheelViewChange(WheelView wheel, int oldValue, int newValue) {
        //记录当前的日期 选择的 位置
        tempDatePosition = newValue;
        //获取当前选中的值
        if (newValue == 0) {
            //选择到了日期的第一个
            hourAdapter.setNewData(tempHourData = hourDataStart);
            if (tempHourData.contains(hour)) {
                //当前的  tempHourData  中有上一次选中的  hour
                //那么就自动选回上一次的值
                secondWheelView.setCurrentItem(tempHourData.indexOf(hour));
            } else {
                //没有就选择到第0个位置
                secondWheelView.setCurrentItem(0);
                //更新  hour
                hour = tempHourData.get(0);
            }
        } else if (newValue == tempDateData.size() - 1) {
            //最后一个
            hourAdapter.setNewData(tempHourData = hourDataEnd);
            if (tempHourData.contains(hour)) {
                //当前的  tempHourData  中有上一次选中的  hour
                //那么就自动选回上一次的值
                secondWheelView.setCurrentItem(tempHourData.indexOf(hour));
            } else {
                //没有就选择到第0个位置
                secondWheelView.setCurrentItem(0);
                //更新  hour
                hour = tempHourData.get(0);
            }
        } else {
            //中间的情况  直接选择满的值
            hourAdapter.setNewData(tempHourData = hourData);
            secondWheelView.setCurrentItem(tempHourData.indexOf(hour));
            minuteAdapter.setNewData(tempMinuteData = minuteData);
            thirdWheelView.setCurrentItem(tempMinuteData.indexOf(minute));
        }

        //再根据当前的  日期和时  选择的位置   去处理分钟的位置
        disposeMinute(secondWheelView.getCurrentItem());
    }

    @Override
    protected void secondWheelViewChange(WheelView wheel, int oldValue, int newValue) {
        //获取选中的hour的值
        hour = tempHourData.get(newValue);
        //根据当前时的位置   去处理分应该选择的位置
        disposeMinute(newValue);
    }

    /**
     * 处理分的逻辑
     *
     * @param newValue
     */
    private void disposeMinute(int newValue) {
        if (newValue == 0 && tempDatePosition == 0) {
            //第一天的第一小时的情况
            minuteAdapter.setNewData(tempMinuteData = minuteDataStart);
            if (tempMinuteData.contains(minute)) {
                thirdWheelView.setCurrentItem(tempMinuteData.indexOf(minute));
            } else {
                thirdWheelView.setCurrentItem(0);
                minute = tempMinuteData.get(0);
            }
        } else if (newValue == tempHourData.size() - 1 && tempDatePosition == tempDateData.size() - 1) {
            //最后一天的最后一小时的情况
            minuteAdapter.setNewData(tempMinuteData = minuteDataEnd);
            if (tempMinuteData.contains(minute)) {
                thirdWheelView.setCurrentItem(tempMinuteData.indexOf(minute));
            } else {
                thirdWheelView.setCurrentItem(0);
                minute = tempMinuteData.get(0);
            }
        } else {
            //中间的情况  直接选择满的值
            minuteAdapter.setNewData(tempMinuteData = minuteData);
            thirdWheelView.setCurrentItem(tempMinuteData.indexOf(minute));
        }
    }

    @Override
    protected void thirdWheelViewChange(WheelView wheel, int oldValue, int newValue) {
        //直接获取当前选择的分的值
        minute = tempMinuteData.get(newValue);
    }
}
