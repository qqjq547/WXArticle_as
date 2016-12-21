package com.hereafter.wxarticle.http;

import java.io.IOException;
import java.lang.ref.WeakReference;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ResponeCallback implements com.squareup.okhttp.Callback {
	private static final int CALLBACK_SUCCESSFUL = 0x01;
	private static final int CALLBACK_FAILED = 0x02;

	static class UIHandler extends Handler {
		private WeakReference mWeakReference;

		public UIHandler(ResponeCallback callback) {
			super(Looper.getMainLooper());
			mWeakReference = new WeakReference(callback);

		}

		@Override
		public void handleMessage(Message msg) {
			String response = (String) msg.obj;
			ResponeCallback callback = (ResponeCallback) mWeakReference.get();
			switch (msg.what) {
			case CALLBACK_SUCCESSFUL: {
				if (callback != null) {
					callback.onSuccess(response);
				}
				break;
			}
			case CALLBACK_FAILED: {
				if (callback != null) {
					callback.onFailure(response);
				}
				break;
			}
			default:
				super.handleMessage(msg);
				break;
			}
		}
	}

	private Handler mHandler = new UIHandler(this);

	@Override
	public void onFailure(Request request, IOException e) {
		Message message = Message.obtain();
		message.what = CALLBACK_FAILED;
		message.obj = e.getMessage();
		mHandler.sendMessage(message);
	}

	@Override
	public void onResponse(Response response) throws IOException {
		Message message = Message.obtain();
		message.obj = response.body().string();
		if (response.isSuccessful()) {
			message.what = CALLBACK_SUCCESSFUL;
		} else {
			message.what = CALLBACK_FAILED;
		}
		mHandler.sendMessage(message);
	}

	public void onSuccess(String result) {

	}

	public void onFailure(String errmsg) {

	}
}
