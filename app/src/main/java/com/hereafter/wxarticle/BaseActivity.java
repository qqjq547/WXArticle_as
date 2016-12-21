package com.hereafter.wxarticle;

import com.hereafter.wxarticle.app.MyApplication;
import com.hereafter.wxarticle.util.Utils;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity implements OnClickListener, Handler.Callback {
	private Handler mHandler = new Handler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		PushAgent.getInstance(this).onAppStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		PgyFeedbackShakeManager.register(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		PgyFeedbackShakeManager.unregister();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.getInstance().removeActivity(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void showLongToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	public void showLongToast(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
	}

	public void showShortToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void showShortToast(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
	}

	public Toolbar initToolBar(String title) {
		Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
		toolbar.setTitle(title);

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			int statusBarHeight = Utils.getStatusBarHeight(this);
			toolbar.setPadding(0, statusBarHeight, 0, 0);
		}
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		return toolbar;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void sendMessage(Message msg) {
		mHandler.sendMessage(msg);

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	public Handler getHandler() {
		return mHandler;
	}

}
