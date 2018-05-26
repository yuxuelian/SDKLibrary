package com.kaibo.wheelview.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.kaibo.core.BaseApplication;
import com.kaibo.wheelview.data.dao.CityDao;
import com.kaibo.wheelview.data.entity.CityEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Administrator
 * @date 2018/4/23 0023 下午 5:35
 * GitHub：
 * email：
 * description：
 */

@Database(entities = {CityEntity.class}, version = 1, exportSchema = false)
public abstract class BaseCityDatabase extends RoomDatabase {

    private static final String CITY_NAME = "city.db";

    static {
        //获取一下数据库文件
        File database = BaseApplication.Companion.getInstance().getDatabasePath(BaseCityDatabase.CITY_NAME);
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            //如果不存在的话,就进行copy
            if (!database.exists() && database.createNewFile()) {
                byte[] buf = new byte[1024];
                fileOutputStream = new FileOutputStream(database);
                inputStream = BaseApplication.Companion.getInstance().getAssets().open("city.db");
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static BaseCityDatabase baseCityDatabase;

    public static BaseCityDatabase getInstance() {
        if (baseCityDatabase == null) {
            synchronized (BaseCityDatabase.class) {
                if (baseCityDatabase == null) {
                    baseCityDatabase = Room.databaseBuilder(BaseApplication.Companion.getInstance(), BaseCityDatabase.class, "city.db").build();
                }
            }
        }
        return baseCityDatabase;
    }

    /**
     * 访问city表
     *
     * @return
     */
    public abstract CityDao cityDao();
}
