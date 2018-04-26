package com.kaibo.wheelview.data.entity;

import java.util.List;

/**
 * @author Administrator
 * @date 2018/4/24 0024 下午 3:42
 * GitHub：
 * email：
 * description：
 */
public class ProvinceBean extends BaseAddressBean {

    public String name;
    public List<LeaderBean> leaderBeanList;

    public ProvinceBean(String name, List<LeaderBean> leaderBeanList) {
        this.name = name;
        this.leaderBeanList = leaderBeanList;
    }

    @Override
    public String getName() {
        return name;
    }
}
