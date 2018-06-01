package com.kaibo.wheelview.dialog;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;

import com.kaibo.wheelview.adapter.SelectCityWheelAdapter;
import com.kaibo.wheelview.adapter.WheelViewAdapter;
import com.kaibo.wheelview.data.BaseCityDatabase;
import com.kaibo.wheelview.data.dao.CityDao;
import com.kaibo.wheelview.data.entity.CityBean;
import com.kaibo.wheelview.data.entity.LeaderBean;
import com.kaibo.wheelview.data.entity.ProvinceBean;
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
 * 邮箱:568966289@qq.com
 * <p>
 * 创建时间:2017/4/19 14:44
 * 备注:
 *
 * @author:Administrator
 */

public class SelectCityWheelDialog extends BaseWheelDialog {

    /**
     * 保存选中城市信息
     */
    private String selectCity;
    private String selectCityId;
    private String selectLeader;
    private String selectProvince;

    /**
     * 三个适配器
     */
    private SelectCityWheelAdapter<CityBean> cityAdapter;
    private SelectCityWheelAdapter<LeaderBean> leaderAdapter;
    private SelectCityWheelAdapter<ProvinceBean> provinceAdapter;

    /**
     * 临时辅助变量
     */
    private List<LeaderBean> currentLeaders;

    private OnSelectedListener onSelectedListener;

    /**
     * 保存所有的城市信息
     */
    private List<ProvinceBean> allCityMsg;

    public interface OnSelectedListener {
        /**
         * 点击确定后回调这个方法
         *
         * @param selectProvince
         * @param selectLeader
         * @param selectCity
         * @param selectCityId
         */
        void onComplete(String selectProvince, String selectLeader, String selectCity, String selectCityId);
    }

    public void setSelectedListener(OnSelectedListener listener) {
        this.onSelectedListener = listener;
    }

    public SelectCityWheelDialog(@NonNull DialogStyleConfig dialogStyleConfig) {
        super(dialogStyleConfig);
        initData();
    }

    @SuppressLint("CheckResult")
    private void initData() {
        final CityDao cityDao = BaseCityDatabase.getInstance().cityDao();
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
                        CityBean cityBean = provinceBeans.get(0).leaderBeanList.get(0).cityBeanList.get(0);
                        selectCity = cityBean.name;
                        selectCityId = cityBean.cityId;

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
                        //新数据加载完成设置新数据
                        provinceAdapter.setNewData(allCityMsg);
                        leaderAdapter.setNewData(allCityMsg.get(0).leaderBeanList);
                        cityAdapter.setNewData(allCityMsg.get(0).leaderBeanList.get(0).cityBeanList);
                    }
                });
    }

    @NonNull
    @Override
    protected WheelViewAdapter getFirstWheelAdapter() {
        //创建适配器   这个地方只能使用   getContext()  不能通过构造方法将context获取到
        //因为三个方法在构造方法里边的super中执行
        this.provinceAdapter = new SelectCityWheelAdapter<>(getContext());
        return provinceAdapter;
    }

    @NonNull
    @Override
    protected WheelViewAdapter getSecondWheelAdapter() {
        this.leaderAdapter = new SelectCityWheelAdapter<>(getContext());
        return leaderAdapter;
    }

    @NonNull
    @Override
    protected WheelViewAdapter getThirdWheelAdapter() {
        this.cityAdapter = new SelectCityWheelAdapter<>(getContext());
        return cityAdapter;
    }

    @Override
    protected void firstWheelViewChange(WheelView wheel, int oldValue, int newValue) {
        selectProvince = provinceAdapter.getData(wheel.getCurrentItem()).name;
        //更新市
        currentLeaders = allCityMsg.get(newValue).leaderBeanList;
        selectLeader = currentLeaders.get(0).name;

        leaderAdapter.setNewData(currentLeaders);
        secondWheelView.setViewAdapter(leaderAdapter);
        secondWheelView.setCurrentItem(0);

        //根据市，地区联动
        List<CityBean> cityBeanList = currentLeaders.get(0).cityBeanList;
        CityBean cityBean = cityBeanList.get(0);
        selectCity = cityBean.name;
        selectCityId = cityBean.cityId;
        cityAdapter.setNewData(cityBeanList);
        thirdWheelView.setViewAdapter(cityAdapter);
        thirdWheelView.setCurrentItem(0);
    }

    @Override
    protected void secondWheelViewChange(WheelView wheel, int oldValue, int newValue) {
        selectLeader = leaderAdapter.getData(wheel.getCurrentItem()).name;
        //根据市，地区联动
        List<CityBean> cityBeanList = currentLeaders.get(newValue).cityBeanList;
        CityBean cityBean = cityBeanList.get(0);
        selectCity = cityBean.name;
        selectCityId = cityBean.cityId;
        cityAdapter.setNewData(cityBeanList);
        thirdWheelView.setViewAdapter(cityAdapter);
        thirdWheelView.setCurrentItem(0);
    }

    @Override
    protected void thirdWheelViewChange(WheelView wheel, int oldValue, int newValue) {
        //滚动市的时候
        CityBean cityBean = cityAdapter.getData(wheel.getCurrentItem());
        selectCity = cityBean.name;
        selectCityId = cityBean.cityId;
    }

    @Override
    protected boolean rightBtnClick(View completeBtn) {
        if (onSelectedListener != null) {
            onSelectedListener.onComplete(selectProvince, selectLeader, selectCity, selectCityId);
        }
        return super.rightBtnClick(completeBtn);
    }
}
