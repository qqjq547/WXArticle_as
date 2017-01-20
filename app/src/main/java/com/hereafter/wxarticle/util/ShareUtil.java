package com.hereafter.wxarticle.util;

import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.http.HttpUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import android.app.Activity;
import android.content.Intent;

public class ShareUtil {
	private Activity activity;
	private UMImage mUmImage;
	private String share_content;
	private String share_title;
	private String share_url;

	public ShareUtil(Activity activity) {
		this.activity = activity;
		initData(activity);
	}

	public ShareUtil(Activity activity, String url) {
		this.activity = activity;
		share_url = url;
		initData(activity);
	}

	public ShareUtil(Activity activity, String title, String linkurl) {
		this.activity = activity;
		share_title = title;
		share_content = activity.getString(R.string.default_sharecontent);
		mUmImage = new UMImage(activity, R.drawable.app_icon);
		share_url = linkurl;
	}

	public void initData(final Activity activity) {
		share_title = activity.getString(R.string.share_title);
		share_content = activity.getString(R.string.default_sharecontent);
		mUmImage = new UMImage(activity, R.drawable.app_icon);
		share_url = HttpUtil.URL_SHARE_BASE;
	}

	private UMShareListener umShareListener = new UMShareListener() {

		@Override
		public void onResult(SHARE_MEDIA arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(SHARE_MEDIA arg0, Throwable arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancel(SHARE_MEDIA arg0) {
			// TODO Auto-generated method stub

		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
	};

	public void show() {
		new ShareAction(activity)
				.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
						SHARE_MEDIA.QZONE)
				.withText(share_content).withTitle(share_title).withTargetUrl(share_url).withMedia(mUmImage)
				.setListenerList(umShareListener, umShareListener).open();
	}

}
