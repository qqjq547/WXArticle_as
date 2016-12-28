package com.hereafter.wxarticle;

import com.hereafter.wxarticle.util.ThirdLoginUtil;
import com.hereafter.wxarticle.util.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {
	@ViewInject(R.id.username)
	private EditText username;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.forgetpwd)
	private TextView forgetpwd;
	@ViewInject(R.id.login)
	private TextView login;
	@ViewInject(R.id.register)
	private TextView register;
	@ViewInject(R.id.login_sina)
	private ImageView login_sina;
	@ViewInject(R.id.login_wechat)
	private ImageView login_wechat;
	@ViewInject(R.id.login_qq)
	private ImageView login_qq;
	@ViewInject(R.id.login_other)
	private LinearLayout login_other;
	private ThirdLoginUtil loginutil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		forgetpwd.setOnClickListener(this);
		login_other.setOnClickListener(this);
		login_sina.setOnClickListener(this);
		login_wechat.setOnClickListener(this);
		login_qq.setOnClickListener(this);
		MobclickAgent.setSessionContinueMillis(1000);
		MobclickAgent.setDebugMode(true);
		if (BmobUser.getCurrentUser(this) != null) {
			finish();
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
		}
		loginutil = new ThirdLoginUtil(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.login:
			String phone = username.getText().toString().trim();
			String pwd = password.getText().toString().trim();
			if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(phone)) {
				showLongToast(R.string.login_emptyphone);
			} else if (!Utils.isMobileNO(phone)) {
				showLongToast(R.string.login_inputrightphone);
			} else {
				BmobUser user = new BmobUser();
				user.setUsername(phone);
				user.setPassword(pwd);
				user.login(this, new SaveListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						showLongToast(R.string.login_success);
						finish();
					}

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						showLongToast(R.string.login_failed);
					}
				});

			}

			break;
		case R.id.forgetpwd:
			startActivity(new Intent(this, ForgetPwdActivity.class));
			break;
		case R.id.register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		case R.id.login_sina:
			loginutil.showSinaAuth();
			break;
		case R.id.login_wechat:
			loginutil.showWeChatAuth();
			;
			break;
		case R.id.login_qq:
			loginutil.showQQAuth();
			;
			break;
		case R.id.login_other:
			startActivity(new Intent(LoginActivity.this, MainActivity.class));

			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		loginutil.onActivityResult(arg0, arg1, arg2);
	}

}
