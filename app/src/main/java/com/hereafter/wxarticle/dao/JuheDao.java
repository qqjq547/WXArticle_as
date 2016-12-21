package com.hereafter.wxarticle.dao;

import java.util.ArrayList;

import com.hereafter.wxarticle.bmob.Juhe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class JuheDao {
	private DBHelper dbHelper;
	private static final String TABLE_NAME = "juhe";
	private static JuheDao mDao = null;
	private SQLiteDatabase db = null;
	private static String ID = "id";
	private static String TITLE = "title";
	private static String SOURCE = "source";
	private static String FIRSTIMG = "firstImg";
	private static String MARK = "mark";
	private static String URL = "url";

	public static synchronized JuheDao getInstance(Context context) {
		if (mDao == null) {
			mDao = new JuheDao(context);
		}
		return mDao;
	}

	private JuheDao(Context context) {
		super();
		dbHelper = DBHelper.getInstance(context);
		db = dbHelper.getWritableDatabase();
	}

	public boolean isExist(Juhe juhe) {
		Juhe mJuhe = query(juhe.getId());
		if (mJuhe == null) {
			return false;
		}
		return true;
	}

	public int insert(Juhe juhe) {
		if (isExist(juhe)) {
			return update(juhe);
		}
		int id = -1;
		synchronized (dbHelper) {
			// 看数据库是否关闭
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			// 开始事务
			db.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				values.put(ID, juhe.getId());
				values.put(TITLE, juhe.getTitle());
				values.put(SOURCE, juhe.getSource());
				values.put(FIRSTIMG, juhe.getFirstImg());
				values.put(MARK, juhe.getMark());
				values.put(URL, juhe.getUrl());
				id = (int) db.insert(TABLE_NAME, null, values);
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
		return id;
	}

	public int updateAll(ArrayList<Juhe> juhes) {
		int count = 0;
		for (int i = 0; i < juhes.size(); i++) {
			int index = update(juhes.get(i));
			if (index >= 0) {
				count++;
			}
		}
		return count;
	}

	public int update(Juhe juhe) {
		int id = -1;
		synchronized (dbHelper) {
			if (!isExist(juhe)) {
				id = insert(juhe);
				return id;
			}
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				values.put(ID, juhe.getId());
				values.put(TITLE, juhe.getTitle());
				values.put(SOURCE, juhe.getSource());
				values.put(FIRSTIMG, juhe.getFirstImg());
				values.put(MARK, juhe.getMark());
				values.put(URL, juhe.getUrl());
				id = db.update(TABLE_NAME, values, ID + "=?", new String[] { juhe.getId() });
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
		return id;
	}

	public boolean insertAll(ArrayList<Juhe> items) {
		if (getcount() > 0) {
			deleteAll();
		}
		for (int i = 0; i < items.size(); i++) {
			int id = insert(items.get(i));
			if (id < 0) {
				return false;
			}
		}
		return true;
	}

	public Juhe query(String id) {
		Juhe mJuhe = null;
		synchronized (dbHelper) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			db.beginTransaction();
			Cursor cursor = db.query(TABLE_NAME, null, "id = ?", new String[] { id }, null, null, null);
			try {
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					String title = cursor.getString(cursor.getColumnIndex(TITLE));
					String source = cursor.getString(cursor.getColumnIndex(SOURCE));
					String firstimg = cursor.getString(cursor.getColumnIndex(FIRSTIMG));
					String mark = cursor.getString(cursor.getColumnIndex(MARK));
					String url = cursor.getString(cursor.getColumnIndex(URL));
					mJuhe = new Juhe(id, title, source, firstimg, mark, url);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
				cursor.close();
			}
		}
		return mJuhe;
	}

	public synchronized ArrayList<Juhe> queryAll() {
		ArrayList<Juhe> Juhes = new ArrayList<Juhe>();
		synchronized (dbHelper) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			db.beginTransaction();
			Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "id desc");
			try {
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						String id = cursor.getString(cursor.getColumnIndex(ID));
						String title = cursor.getString(cursor.getColumnIndex(TITLE));
						String source = cursor.getString(cursor.getColumnIndex(SOURCE));
						String firstimg = cursor.getString(cursor.getColumnIndex(FIRSTIMG));
						String mark = cursor.getString(cursor.getColumnIndex(MARK));
						String url = cursor.getString(cursor.getColumnIndex(URL));
						Juhes.add(new Juhe(id, title, source, firstimg, mark, url));
					}
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
				cursor.close();
			}
		}
		return Juhes;
	}

	public void deleteAll() {
		synchronized (dbHelper) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.delete(TABLE_NAME, null, null);
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}

	public void deleteById(int tid) {
		synchronized (dbHelper) {
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			db.beginTransaction();

			try {
				db.delete(TABLE_NAME, "id=?", new String[] { String.valueOf(tid) });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}

	public int getcount() {
		return queryAll().size();
	}
}
