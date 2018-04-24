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

import com.kaibo.wheelview.adapter.AddressTextAdapter;
import com.kaibo.wheelview.data.BaseCityDatabase;
import com.kaibo.wheelview.data.dao.CityDao;
import com.kaibo.wheelview.data.entity.CityBean;
import com.kaibo.wheelview.data.entity.LeaderBean;
import com.kaibo.wheelview.data.entity.ProvinceBean;
import com.kaibo.wheelview.listener.OnWheelChangedListener;
import com.kaibo.wheelview.weight.WheelView;

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
 *
 * @author Administrator
 */

public class CityWheelDialog extends Dialog {

    private WheelView wvCity;
    private WheelView wvLeader;
    private WheelView wvProvince;

    private String selectCity;
    private String selectCityId;
    private String selectLeader;
    private String selectProvince;

    private AddressTextAdapter<CityBean> cityAdapter;
    private AddressTextAdapter<LeaderBean> leaderAdapter;
    private AddressTextAdapter<ProvinceBean> provinceAdapter;

    private List<LeaderBean> currentLeaders;
    private OnCitySelectedListener listener;

    /**
     * 保存所有的城市信息
     */
    private List<ProvinceBean> allCityMsg;

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnCitySelectedListener {
        /**
         * 点击确定后回调这个方法
         *
         * @param strProvince
         * @param strLeader
         * @param strCity
         * @param cityId
         */
        void onComplete(String strProvince, String strLeader, String strCity, String cityId);
    }

    public void setSelectedListener(OnCitySelectedListener listener) {
        this.listener = listener;
    }

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
        initData(context);
        initView(context);
    }

    @SuppressLint("CheckResult")
    private void initData(final Context context) {
        final CityDao cityDao = BaseCityDatabase.getInstance(context).cityDao();
        Observable
                .create(new ObservableOnSubscribe<List<ProvinceBean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<ProvinceBean>> emitter) {
                        List<ProvinceBean> provinceBeans = new ArrayList<>();
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
                            provinceBeans.add(new ProvinceBean(provinceZh, leaderBeanList));
                        }
                        //初始化选中
                        selectProvince = provinceBeans.get(0).name;
                        selectLeader = provinceBeans.get(0).leaderBeanList.get(0).name;
                        selectCity = provinceBeans.get(0).leaderBeanList.get(0).cityBeanList.get(0).name;
                        emitter.onNext(provinceBeans);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ProvinceBean>>() {
                    @Override
                    public void accept(List<ProvinceBean> provinceBeans) throws Exception {
                        allCityMsg = provinceBeans;
                        provinceAdapter.setNewData(allCityMsg);
                        initWheelView(context);
                    }
                });
    }

    private void initView(final Context context) {
        Window win = this.getWindow();
        if (win != null) {
            win.setBackgroundDrawableResource(R.color.white);
            win.setWindowAnimations(R.style.mypopwindow_anim_style);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            win.setAttributes(lp);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.edit_changeaddress_dialog_layout, (ViewGroup) this.getWindow().getDecorView(), false);
        wvProvince = (WheelView) view.findViewById(R.id.wv_address_province);
        wvLeader = (WheelView) view.findViewById(R.id.wv_address_city);
        wvCity = (WheelView) view.findViewById(R.id.wv_address_area);
        view.findViewById(R.id.complete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onComplete(selectProvince, selectLeader, selectCity, selectCityId);
                }
                dismiss();
            }
        });
        view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
    }

    private void initWheelView(Context context) {
        provinceAdapter = new AddressTextAdapter<>(context, allCityMsg);
        wvProvince.setVisibleItems(5);
        wvProvince.setViewAdapter(provinceAdapter);

        leaderAdapter = new AddressTextAdapter<>(context, allCityMsg.get(0).leaderBeanList);
        wvLeader.setVisibleItems(5);
        wvLeader.setViewAdapter(leaderAdapter);

        cityAdapter = new AddressTextAdapter<>(context, allCityMsg.get(0).leaderBeanList.get(0).cityBeanList);
        wvCity.setVisibleItems(5);
        wvCity.setViewAdapter(cityAdapter);

        wvProvince.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectProvince = provinceAdapter.getData(wheel.getCurrentItem()).name;
                //更新市
                currentLeaders = allCityMsg.get(newValue).leaderBeanList;
                selectLeader = currentLeaders.get(0).name;

                leaderAdapter.setNewData(currentLeaders);
                wvLeader.setVisibleItems(5);
                wvLeader.setViewAdapter(leaderAdapter);
                wvLeader.setCurrentItem(0);

                //根据市，地区联动
                List<CityBean> cityBeanList = currentLeaders.get(0).cityBeanList;
                CityBean cityBean = cityBeanList.get(0);
                selectCity = cityBean.name;
                selectCityId = cityBean.cityId;
                cityAdapter.setNewData(cityBeanList);
                wvCity.setVisibleItems(5);
                wvCity.setViewAdapter(cityAdapter);
                wvCity.setCurrentItem(0);
            }
        });

        wvLeader.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectLeader = leaderAdapter.getData(wheel.getCurrentItem()).name;
                //根据市，地区联动
                List<CityBean> cityBeanList = currentLeaders.get(newValue).cityBeanList;
                CityBean cityBean = cityBeanList.get(0);
                selectCity = cityBean.name;
                selectCityId = cityBean.cityId;
                cityAdapter.setNewData(cityBeanList);
                wvCity.setVisibleItems(5);
                wvCity.setViewAdapter(cityAdapter);
                wvCity.setCurrentItem(0);
            }
        });

        wvCity.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectCity = cityAdapter.getData(wheel.getCurrentItem()).name;
            }
        });
    }
}
