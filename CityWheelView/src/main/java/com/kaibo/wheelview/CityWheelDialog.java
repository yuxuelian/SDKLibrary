package com.kaibo.wheelview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kaibo.wheelview.data.entity.CityBean;
import com.kaibo.wheelview.listener.OnWheelChangedListener;
import com.kaibo.wheelview.listener.OnWheelScrollListener;
import com.kaibo.wheelview.weight.WheelView;
import com.kaibo.wheelview.adapter.AbstractWheelTextAdapter;
import com.kaibo.wheelview.data.BaseCityDatabase;
import com.kaibo.wheelview.data.dao.CityDao;
import com.kaibo.wheelview.data.entity.BaseAddressBean;
import com.kaibo.wheelview.data.entity.LeaderBean;
import com.kaibo.wheelview.data.entity.ProvinceBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 邮箱：568966289@qq.com
 * <p>
 * 创建时间：2017/4/19 14:44
 * 备注：
 */

public class CityWheelDialog extends Dialog {

    private TextView mButtonGetCancel;
    private TextView mButtonGetComplete;

    private WheelView wvCity;
    private WheelView wvLeader;
    private WheelView wvProvince;

    private String selectCity;
    private String selectLeader;
    private String selectProvince;

    private AddressTextAdapter<CityBean> cityAdapter;
    private AddressTextAdapter<LeaderBean> leaderAdapter;
    private AddressTextAdapter<ProvinceBean> provinceAdapter;

    private OnCitySelectedListener listener;
    private List<CityBean> cityBeanList;
    private List<LeaderBean> currentLeaders;

    public void setSelectedListener(OnCitySelectedListener listener) {
        this.listener = listener;
    }

    private int maxSize = 16;
    private int minSize = 14;

    public CityWheelDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CityWheelDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected CityWheelDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @SuppressLint("CheckResult")
    private void init(final Context context) {
        Observable<List<ProvinceBean>> listObservable = initData(context);
        listObservable.subscribe(new Consumer<List<ProvinceBean>>() {
            @Override
            public void accept(List<ProvinceBean> provinceBeans) throws Exception {
                initView(context);
            }
        });
    }

    private void initView(final Context context) {
        initWindow();
        View view = LayoutInflater.from(context).inflate(R.layout.edit_changeaddress_dialog_layout, (ViewGroup) this.getWindow().getDecorView(), false);
        wvProvince = (WheelView) view.findViewById(R.id.wv_address_province);
        wvLeader = (WheelView) view.findViewById(R.id.wv_address_city);
        wvCity = (WheelView) view.findViewById(R.id.wv_address_area);
        mButtonGetComplete = (TextView) view.findViewById(R.id.complete_btn);
        mButtonGetCancel = (TextView) view.findViewById(R.id.cancel_btn);
        mButtonGetComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onComplete(selectProvince, selectLeader, selectCity);
                }
                dismiss();
            }
        });
        mButtonGetCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);

        Window win = this.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        provinceAdapter = new AddressTextAdapter<>(context, allCityMsg, 0, maxSize, minSize);
        wvProvince.setVisibleItems(5);
        wvProvince.setViewAdapter(provinceAdapter);

        leaderAdapter = new AddressTextAdapter<>(context, allCityMsg.get(0).leaderBeanList, 0, maxSize, minSize);
        wvLeader.setVisibleItems(5);
        wvLeader.setViewAdapter(leaderAdapter);

        cityAdapter = new AddressTextAdapter<>(context, allCityMsg.get(0).leaderBeanList.get(0).cityBeanList, 0, maxSize, minSize);
        wvCity.setVisibleItems(5);
        wvCity.setViewAdapter(cityAdapter);

        wvProvince.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                selectProvince = currentText;
                setTextviewSize(currentText, provinceAdapter);

                //更新市
                currentLeaders = allCityMsg.get(newValue).leaderBeanList;
                selectLeader = currentLeaders.get(0).name;
                leaderAdapter = new AddressTextAdapter<>(context, currentLeaders, 0, maxSize, minSize);
                wvLeader.setVisibleItems(5);
                wvLeader.setViewAdapter(leaderAdapter);
                wvLeader.setCurrentItem(0);
                setTextviewSize("0", leaderAdapter);

                //根据市，地区联动
                List<CityBean> cityBeanList = currentLeaders.get(0).cityBeanList;
                selectCity = cityBeanList.get(0).name;
                cityAdapter = new AddressTextAdapter<>(context, cityBeanList, 0, maxSize, minSize);
                wvCity.setVisibleItems(5);
                wvCity.setViewAdapter(cityAdapter);
                wvCity.setCurrentItem(0);
                setTextviewSize("0", cityAdapter);
            }
        });

        wvProvince.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
            }
        });

        wvLeader.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) leaderAdapter.getItemText(wheel.getCurrentItem());
                selectLeader = currentText;
                setTextviewSize(currentText, leaderAdapter);

                //根据市，地区联动
                List<CityBean> cityBeanList = currentLeaders.get(newValue).cityBeanList;
                selectCity = cityBeanList.get(0).name;

                cityAdapter = new AddressTextAdapter<>(context, cityBeanList, 0, maxSize, minSize);
                wvCity.setVisibleItems(5);
                wvCity.setViewAdapter(cityAdapter);
                wvCity.setCurrentItem(0);
                setTextviewSize("0", cityAdapter);
            }
        });

        wvLeader.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) leaderAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, leaderAdapter);
            }
        });

        wvCity.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                selectCity = currentText;
                setTextviewSize(currentText, leaderAdapter);
            }
        });

        wvCity.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, cityAdapter);
            }
        });
    }

    /**
     * 保存所有的城市信息
     */
    private List<ProvinceBean> allCityMsg;

    @SuppressLint("CheckResult")
    private Observable<List<ProvinceBean>> initData(Context context) {
        final CityDao cityDao = BaseCityDatabase.getInstance(context).cityDao();
        return Observable
                .create(new ObservableOnSubscribe<List<ProvinceBean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<ProvinceBean>> emitter) throws Exception {
                        allCityMsg = new ArrayList<>();
                        List<String> provinceZhs = cityDao.findAllProvinceZh();
                        for (String provinceZh : provinceZhs) {
                            List<LeaderBean> leaderBeanList = new ArrayList<>();
                            //指定省名称查询该省下所有的市
                            List<String> leaders = cityDao.findLeaderZhByProvinceZh(provinceZh);
                            for (String leader : leaders) {
                                //指定市名称查询该市下所有的区
                                List<CityBean> cityZhByLeaderZh = cityDao.findCityZhByLeaderZh(provinceZh, leader);
                                //将市与市下面的所有区对应起来
                                leaderBeanList.add(new LeaderBean(leader, cityZhByLeaderZh));
                            }
                            //将省和省下的所有市对应起来
                            allCityMsg.add(new ProvinceBean(provinceZh, leaderBeanList));
                        }
                        //初始化选中
                        selectProvince = allCityMsg.get(0).name;
                        selectLeader = allCityMsg.get(0).leaderBeanList.get(0).name;
                        selectCity = allCityMsg.get(0).leaderBeanList.get(0).cityBeanList.get(0).name;
                        emitter.onNext(allCityMsg);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void initWindow() {
        Window win = this.getWindow();
        if (win != null) {
            win.setBackgroundDrawableResource(R.color.white);
            win.setWindowAnimations(R.style.mypopwindow_anim_style);
        }
    }


    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    private void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(14);
            } else {
                textvew.setTextSize(12);
            }
        }
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnCitySelectedListener {
        void onComplete(String strProvince, String strLeader, String strCity);
    }

    private class AddressTextAdapter<T extends BaseAddressBean> extends AbstractWheelTextAdapter {

        List<T> list;

        protected AddressTextAdapter(Context context, List<T> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index).getName();
        }
    }

}
