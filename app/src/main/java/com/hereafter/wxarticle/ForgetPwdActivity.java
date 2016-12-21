package com.hereafter.wxarticle;

import com.hereafter.wxarticle.util.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;

public class ForgetPwdActivity extends BaseActivity {
	@ViewInject(R.id.username)
	private EditText username;
	@ViewInject(R.id.inputcode)
	private EditText inputcode;
	@ViewInject(R.id.verify)
	private TextView verify;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.repassword)
	private EditText repassword;
	@ViewInject(R.id.submit)
	private TextView submit;
	private int currentsec = 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpwd);
		ViewUtils.inject(this);
		initToolBar(getString(R.string.login_forgetpassword));
		submit.setOnClickListener(this);
		verify.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		String phone = username.getText().toString().trim();
		String code = inputcode.getText().toString().trim();
		String psw = password.getText().toString().trim();
		String repsw = repassword.getText().toString().trim();
		switch (v.getId()) {
		case R.id.submit:
			if (!Utils.isMobileNO(phone)) {
				showShortToast(R.string.regist_rightphone);
			} else if (psw.length() < 6 || psw.length() > 20) {
				showShortToast(R.string.regist_rightpwd);
			} else if (code.length() != 6) {
				showShortToast(R.string.regist_rightcode);
			} else if (!psw.equals(repsw)) {
				showShortToast(R.string.forgetpwd_same);
			} else {
				BmobUser.resetPasswordBySMSCode(this, code, psw, new ResetPasswordByCodeListener() {

					@Override
					public void done(BmobException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							showLongToast(R.string.forgetpwd_success);
							finish();
						} else {
							showLongToast(e.getLocalizedMessage());
						}
					}
				});
			}

			break;
		case R.id.verify:
			if (!Utils.isMobileNO(phone)) {
				showShortToast(R.string.regist_rightphone);
			} else {
				BmobSMS.requestSMSCode(this, phone, getString(R.string.sms_template), new RequestSMSCodeListener() {
					@Override
					public void done(Integer smsId, BmobException ex) {
						// TODO Auto-generated method stub
						if (ex == null) {// 验证码发送成功
							getHandler().sendEmptyMessage(0);
						} else {
							showLongToast(ex.getMessage());
						}
					}
				});
			}
			break;

		default:
			break;
		}
	}

	public boolean signData() {

		return true;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (currentsec == 0) {
			verify.setText(R.string.register_getverify_code);
			verify.setFocusable(true);
			currentsec = 60;
		} else {
			verify.setText(currentsec + getString(R.string.sms_afterget));
			verify.setFocusable(false);
			currentsec--;
			getHandler().sendEmptyMessageDelayed(0, 1000);
		}
		return false;
	}
}
