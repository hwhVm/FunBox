package com.beini.db.greendao;

import android.database.sqlite.SQLiteDatabase;

import com.beini.app.GlobalApplication;


/**
 * Created by beini on 2017/4/5.
 */

public class GreenDaoManage {

//    private static DaoMaster.DevOpenHelper mHelper;
    private static SQLiteDatabase db;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private volatile static GreenDaoManage instance; //声明成 volatile

    private GreenDaoManage() {
    }

    public static GreenDaoManage getSingleton() {
        if (instance == null) {
            synchronized (GreenDaoManage.class) {
                if (instance == null) {
                    instance = new GreenDaoManage();
//                    mHelper = new DaoMaster.DevOpenHelper(GlobalApplication.getInstance(), DB_NAME, null);
                    db = new DBHelper(GlobalApplication.getInstance(), null).getWritableDatabase();
                    // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
                    mDaoMaster = new DaoMaster(db);
                    mDaoSession = mDaoMaster.newSession();
                }
            }
        }
        return instance;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }

}
