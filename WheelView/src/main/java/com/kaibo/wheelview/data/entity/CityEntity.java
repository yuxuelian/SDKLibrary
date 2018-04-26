package com.kaibo.wheelview.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * @author Administrator
 * @date 2018/4/23 0023 下午 5:36
 * GitHub：
 * email：
 * description：数据库中表对应的实体
 */

@Entity(tableName = "city")
public class CityEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NotNull
    private String id;

    @ColumnInfo(name = "cityEn")
    private String cityEn;

    @ColumnInfo(name = "cityZh")
    private String cityZh;

    @ColumnInfo(name = "countryCode")
    private String countryCode;

    @ColumnInfo(name = "countryEn")
    private String countryEn;

    @ColumnInfo(name = "countryZh")
    private String countryZh;

    @ColumnInfo(name = "provinceEn")
    private String provinceEn;

    @ColumnInfo(name = "provinceZh")
    private String provinceZh;

    @ColumnInfo(name = "leaderEn")
    private String leaderEn;

    @ColumnInfo(name = "leaderZh")
    private String leaderZh;

    public CityEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityEn() {
        return cityEn;
    }

    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }

    public String getCityZh() {
        return cityZh;
    }

    public void setCityZh(String cityZh) {
        this.cityZh = cityZh;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getCountryZh() {
        return countryZh;
    }

    public void setCountryZh(String countryZh) {
        this.countryZh = countryZh;
    }

    public String getProvinceEn() {
        return provinceEn;
    }

    public void setProvinceEn(String provinceEn) {
        this.provinceEn = provinceEn;
    }

    public String getProvinceZh() {
        return provinceZh;
    }

    public void setProvinceZh(String provinceZh) {
        this.provinceZh = provinceZh;
    }

    public String getLeaderEn() {
        return leaderEn;
    }

    public void setLeaderEn(String leaderEn) {
        this.leaderEn = leaderEn;
    }

    public String getLeaderZh() {
        return leaderZh;
    }

    public void setLeaderZh(String leaderZh) {
        this.leaderZh = leaderZh;
    }
}
