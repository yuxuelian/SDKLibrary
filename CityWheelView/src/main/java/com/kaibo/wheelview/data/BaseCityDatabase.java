package com.kaibo.wheelview.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.kaibo.common.BaseApplication;
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
        File database = BaseApplication.Companion.getInstance().getDatabasePath(BaseCityDatabase.CITY_NAME);
        //如果不存在的话,就进行copy
        if (!database.exists()) {
            try {
                database.createNewFile();
                byte[] buf = new byte[1024];
                FileOutputStream fileOutputStream = new FileOutputStream(database);
                InputStream inputStream = BaseApplication.Companion.getInstance().getAssets().open("city.db");
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static BaseCityDatabase baseCityDatabase;

    public static BaseCityDatabase getInstance(Context context) {
        if (baseCityDatabase == null) {
            synchronized (BaseCityDatabase.class) {
                if (baseCityDatabase == null) {
                    baseCityDatabase = Room.databaseBuilder(context, BaseCityDatabase.class, "city.db").build();
                }
            }
        }
        return baseCityDatabase;
    }


    public abstract CityDao cityDao();
}
