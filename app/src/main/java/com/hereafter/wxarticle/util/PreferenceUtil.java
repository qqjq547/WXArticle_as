package com.hereafter.wxarticle.util;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 本地Preference的存取工具类
 * 
 * @author 黄家强
 */
public class PreferenceUtil {
	private static PreferenceUtil preference = null;
	private SharedPreferences sharedPreference;
	private String packageName = "";
	private static final String ISLOADIMG = "isloadimg"; // 是否加载图片
	private static final String DOUBLE_ClICK_EXIT = "double_click_exit"; // 登录名
	public static final String TEXT_FONT_SIZE = "text_font_size"; // webview字体大小

	public static synchronized PreferenceUtil getInstance(Context context) {
		if (preference == null)
			preference = new PreferenceUtil(context);
		return preference;
	}

	public PreferenceUtil(Context context) {
		packageName = context.getPackageName() + "_preferences";
		sharedPreference = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
	}

	public void setLoadImg(boolean isloadimg) {
		setBoolean(ISLOADIMG, isloadimg);
	}

	public boolean isLoadImg() {
		return getBoolean(ISLOADIMG, true);
	}

	public void setDoubleExit(boolean isdoubleexit) {
		setBoolean(DOUBLE_ClICK_EXIT, isdoubleexit);
	}

	public boolean isDoubleClick() {
		return getBoolean(DOUBLE_ClICK_EXIT, true);
	}

	public void setInt(String name, int value) {
		Editor edit = sharedPreference.edit();
		edit.putInt(name, value);
		edit.commit();
	}

	public void setBoolean(String name, boolean value) {
		Editor edit = sharedPreference.edit();
		edit.putBoolean(name, value);
		edit.commit();
	}

	public void setString(String name, String value) {
		Editor edit = sharedPreference.edit();
		edit.putString(name, value);
		edit.commit();
	}

	public int getInt(String name, int defaultValue) {
		int tid = sharedPreference.getInt(name, defaultValue);
		return tid;
	}

	public boolean getBoolean(String name, boolean defaultValue) {
		boolean tid = sharedPreference.getBoolean(name, defaultValue);
		return tid;
	}

	public String getString(String name, String defaultValue) {
		String tid = sharedPreference.getString(name, defaultValue);
		return tid;
	}

	public void clean(Context context) {
		File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs");
		if (file.exists())
			file.delete();
	}

}
