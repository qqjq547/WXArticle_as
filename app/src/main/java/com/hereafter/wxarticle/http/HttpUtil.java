package com.hereafter.wxarticle.http;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public class HttpUtil {
	public static final String muurl = "http://www.lezhuan.me/apk/lezhuan.apk";
	public static final String URL_SHARE_BASE = "http://www.pgyer.com/kVyv";

	private static final OkHttpClient mOkHttpClient = new OkHttpClient();

	static {
		mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
	}

	public static void get(String url, ResponeCallback callback) {
		Request request = new Request.Builder().url(url).build();
		mOkHttpClient.newCall(request).enqueue(callback);
	}

	public static void post(String url, Map<String, String> params, ResponeCallback callback) {
		FormEncodingBuilder form = new FormEncodingBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			form.add(key, value);
		}
		RequestBody formBody = form.build();
		Request request = new Request.Builder().url(url).post(formBody).build();
		mOkHttpClient.newCall(request).enqueue(callback);
	}

	public static String getUrlStr(String url, Map<String, String> params) {
		url = url + "?";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = params.get(entry.getKey());
			String value = params.get(entry.getValue());
			url = url + key + "=" + value + "&";
		}
		url = url.substring(0, url.length() - 2);
		return url;
	}

}
