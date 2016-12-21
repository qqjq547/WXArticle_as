package com.hereafter.wxarticle.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final int VERSION = 10;
	private SQLiteDatabase db;
	private String DROP_JUHE = "drop table if exists juhe ";
	private String DROP_SHOWAPI = "drop table if exists showapi ";

	private DBHelper(Context context) {
		super(context, "data.db", null, VERSION);
	}

	private static DBHelper mInstance;

	public synchronized static DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	};

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		db.execSQL("create table if not exists juhe(" + "id varchar(50) ," + "title varchar(50),"
				+ "source varchar(50)," + "firstImg varchar(50)," + "mark varchar(20)," + "url varchar(50))");
		db.execSQL("create table if not exists showapi(" + "id varchar(50) ," + "title varchar(50),"
				+ "date varchar(30)," + "typeId varchar(20)," + "typeName varchar(20)," + "contentImg varchar(30),"
				+ "url varchar(50)," + "userLogo varchar(30)," + "userLogo_code varchar(30),"
				+ "userName varchar(30))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL(DROP_JUHE);
		db.execSQL(DROP_SHOWAPI);
		onCreate(db);
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		db.close();
		super.close();
	}
}
