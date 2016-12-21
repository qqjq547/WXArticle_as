package com.hereafter.wxarticle.dao;

import java.util.ArrayList;

import com.hereafter.wxarticle.bmob.ShowApi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ShowApiDao {
	private DBHelper dbHelper;
	private static final String TABLE_NAME = "showapi";
	private static ShowApiDao mDao = null;
	private SQLiteDatabase db = null;
	private String ID = "id";
	private String TITLE = "title";
	private String DATE = "date";
	private String TYPEID = "typeId";
	private String TYPENAME = "typeName";
	private String CONTENTIMG = "contentImg";
	private String URL = "url";
	private String USERLOGO = "userLogo";
	private String USERLOGO_CODE = "userLogo_code";
	private String USERNAME = "userName";

	public static synchronized ShowApiDao getInstance(Context context) {
		if (mDao == null) {
			mDao = new ShowApiDao(context);
		}
		return mDao;
	}

	private ShowApiDao(Context context) {
		super();
		dbHelper = DBHelper.getInstance(context);
		db = dbHelper.getWritableDatabase();
	}

	public boolean isExist(ShowApi article) {
		ShowApi mApi = query(article.getId());
		if (mApi == null) {
			return false;
		}
		return true;
	}

	public int insert(ShowApi article) {
		if (isExist(article)) {
			return update(article);
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
				values.put(ID, article.getId());
				values.put(TITLE, article.getTitle());
				values.put(DATE, article.getDate());
				values.put(TYPEID, article.getTypeId());
				values.put(TYPENAME, article.getTypeName());
				values.put(CONTENTIMG, article.getContentImg());
				values.put(URL, article.getUrl());
				values.put(USERLOGO, article.getUserLogo());
				values.put(USERLOGO_CODE, article.getUserLogo_code());
				values.put(USERNAME, article.getUserName());

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

	public int updateAll(ArrayList<ShowApi> articles) {
		int count = 0;
		for (int i = 0; i < articles.size(); i++) {
			int index = update(articles.get(i));
			if (index >= 0) {
				count++;
			}
		}
		return count;
	}

	public int update(ShowApi article) {
		int id = -1;
		synchronized (dbHelper) {
			if (!isExist(article)) {
				id = insert(article);
				return id;
			}
			if (!db.isOpen()) {
				db = dbHelper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				values.put(ID, article.getId());
				values.put(TITLE, article.getTitle());
				values.put(DATE, article.getDate());
				values.put(TYPEID, article.getTypeId());
				values.put(TYPENAME, article.getTypeName());
				values.put(CONTENTIMG, article.getContentImg());
				values.put(URL, article.getUrl());
				values.put(USERLOGO, article.getUserLogo());
				values.put(USERLOGO_CODE, article.getUserLogo_code());
				values.put(USERNAME, article.getUserName());
				id = db.update(TABLE_NAME, values, ID + "=?", new String[] { article.getId() });
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

	public boolean insertAll(ArrayList<ShowApi> items) {
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

	public ShowApi query(String id) {
		ShowApi article = null;
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
					String date = cursor.getString(cursor.getColumnIndex(DATE));
					String typeId = cursor.getString(cursor.getColumnIndex(TYPEID));
					String typeName = cursor.getString(cursor.getColumnIndex(TYPENAME));
					String contentImg = cursor.getString(cursor.getColumnIndex(CONTENTIMG));
					String url = cursor.getString(cursor.getColumnIndex(URL));
					String userLogo = cursor.getString(cursor.getColumnIndex(USERLOGO));
					String userLogo_code = cursor.getString(cursor.getColumnIndex(USERLOGO_CODE));
					String userName = cursor.getString(cursor.getColumnIndex(USERNAME));
					article = new ShowApi(id, title, typeId, typeName, url, userLogo, userLogo_code, userName, date,
							contentImg);
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
		return article;
	}

	public synchronized ArrayList<ShowApi> queryAll() {
		ArrayList<ShowApi> articles = new ArrayList<ShowApi>();
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
						String date = cursor.getString(cursor.getColumnIndex(DATE));
						String typeId = cursor.getString(cursor.getColumnIndex(TYPEID));
						String typeName = cursor.getString(cursor.getColumnIndex(TYPENAME));
						String contentImg = cursor.getString(cursor.getColumnIndex(CONTENTIMG));
						String url = cursor.getString(cursor.getColumnIndex(URL));
						String userLogo = cursor.getString(cursor.getColumnIndex(USERLOGO));
						String userLogo_code = cursor.getString(cursor.getColumnIndex(USERLOGO_CODE));
						String userName = cursor.getString(cursor.getColumnIndex(USERNAME));
						articles.add(new ShowApi(id, title, typeId, typeName, url, userLogo, userLogo_code, userName,
								date, contentImg));
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
		return articles;
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
