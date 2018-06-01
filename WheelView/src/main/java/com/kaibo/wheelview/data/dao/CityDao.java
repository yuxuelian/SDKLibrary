package com.kaibo.wheelview.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.kaibo.wheelview.data.entity.CityBean;

import java.util.List;

/**
 * @author:Administrator
 * @date:2018/4/24 0024 上午 8:57
 * GitHub:
 * email:
 * description:
 */

@Dao
public interface CityDao {

    /**
     * 查询所有的省列表
     *
     * @return
     */
    @Query("SELECT DISTINCT provinceZh FROM city")
    List<String> findAllProvinceZh();

    /**
     * 指定省查询所有的市
     *
     * @param provinceZh
     * @return
     */
    @Query("SELECT DISTINCT leaderZh FROM city WHERE provinceZh=:provinceZh")
    List<String> findLeaderZhByProvinceZh(String provinceZh);

    /**
     * 指定市查询所有的区
     *
     * @param provinceZh
     * @param leaderZh
     * @return
     */
    @Query("SELECT id,cityZh FROM city WHERE provinceZh=:provinceZh AND leaderZh=:leaderZh")
    List<CityBean> findCityZhByLeaderZh(String provinceZh, String leaderZh);

}
