package com.beini.db.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.beini.db.sqllite.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2016/5/4.
 */
public class DataDao {
	private DataBaseHelper helper;
	private Context context;
	private SQLiteDatabase db;
	private volatile static DataDao instance;

	public static DataDao getSingleton(Context context) {
		if (instance == null) {
			synchronized (DataDao.class) {
				if (instance == null) {
					instance = new DataDao(context);
				}
			}
		}
		return instance;
	}

	public DataDao(Context context) {
		this.context = context;
		helper = DataBaseHelper.getSingleton(context);
	}

//	public void addRecord(Person person) {
//		db = helper.getWritableDatabase();
//		db.execSQL("insert into person(name,number) values(?,?)", new Object[]{person.getName(), person.getNumber()});//id从1开始
//		db.close();
//	}

	public void deteleRecord() {

	}

//	public List<Person> queryRecord() {
//		Cursor cursor = db.rawQuery("select * from person", null);
//		List<Person> persons = new ArrayList<>();
//		while (cursor.moveToNext()) {
//			Person person = new Person();
//			int id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始
//			person.setId(id);
//			String name = cursor.getString(1);//获取第二列的值
//			person.setName(name);
//			String number = cursor.getString(2);//获取第三列的值
//			person.setNumber(number);
//			persons.add(person);
//			System.out.println("id=" + id + "  name=" + name + "  number==" + number);
//		}
//		cursor.close();
//		db.close();
//		return persons;
//	}

	public void updateRecord() {
		db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("name", "oooooooo");
		int i = db.update("person", cv, "name = ?", new String[]{"dd"});
		System.out.println("i===" + i);
		db.close();
	}
}
