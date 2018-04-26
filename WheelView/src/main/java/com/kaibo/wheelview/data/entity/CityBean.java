package com.kaibo.wheelview.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * @author Administrator
 * @date 2018/4/24 0024 下午 3:43
 * GitHub：
 * email：
 * description：
 */

public class CityBean extends BaseAddressBean {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "id")
    public String cityId;

    @ColumnInfo(name = "cityZh")
    public String name;

    public CityBean() {
    }

    @NotNull
    public String getCityId() {
        return cityId;
    }

    public void setCityId(@NotNull String cityId) {
        this.cityId = cityId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
