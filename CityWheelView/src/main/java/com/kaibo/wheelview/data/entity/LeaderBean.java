package com.kaibo.wheelview.data.entity;

import java.util.List;

/**
 * @author Administrator
 * @date 2018/4/24 0024 下午 3:43
 * GitHub：
 * email：
 * description：
 */
public class LeaderBean extends BaseAddressBean {

    public String name;
    public List<CityBean> cityBeanList;

    public LeaderBean(String name, List<CityBean> cityBeanList) {
        this.name = name;
        this.cityBeanList = cityBeanList;
    }

    @Override
    public String getName() {
        return name;
    }
}
