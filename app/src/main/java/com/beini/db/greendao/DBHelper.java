package com.beini.db.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by beini on 2017/4/5.
 */

public class DBHelper extends  DaoMaster.OpenHelper{
    private static String DB_NAME = "beini_db";

    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
