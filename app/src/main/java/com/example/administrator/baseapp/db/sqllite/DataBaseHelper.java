package com.example.administrator.baseapp.db.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.baseapp.utils.BLog;

/**
 * Created by beini on 2016/5/4.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private static final String DB_NAME = "db_name";
	private volatile static DataBaseHelper instance;

	public static DataBaseHelper getSingleton(Context context) {
		if (instance == null) {
			synchronized (DataBaseHelper.class) {
				if (instance == null) {
					instance = new DataBaseHelper(context);
				}
			}
		}
		return instance;
	}

	/**
	 * 第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
	 */
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		BLog.d("DataBaseHelper");
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		BLog.d("onCreate(SQLiteDatabase sqLiteDatabase)");
//		db.execSQL("create table user(id int, name varchar(20))");
//		db.execSQL("CREATE TABLE IF NOT EXISTS person (id integer primary key autoincrement, name varchar(20), number varchar(20))");
		db.execSQL("create table person (id integer primary key autoincrement, name varchar(20), number varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		BLog.d("onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)");
		db.execSQL("ALTER TABLE person ADD phone VARCHAR(12)"); //往表中增加一列
	}


	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		BLog.d("每次成功打开数据库后首先被执行");
	}
}
