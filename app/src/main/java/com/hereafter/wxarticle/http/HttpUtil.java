package com.hereafter.wxarticle.http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.hereafter.wxarticle.util.LogUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public class HttpUtil {
	public static final String URL_SHARE_BASE = "http://www.pgyer.com/kVyv";
	public static final String URL_JUHE="http://v.juhe.cn/weixin/query";
	public static final String Juhe_WX_KEY="adf7649c744d28d7eeb5e64049f54383";

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
	public static void getJuheData(int pno,int ps,ResponeCallback callback){
		String url=URL_JUHE+"?"+"pno="+pno+"&ps="+20+"&key="+Juhe_WX_KEY;
		get(url,callback);
	}

}
