package com.hereafter.wxarticle;

import com.hereafter.wxarticle.bmob.MyUser;
import com.hereafter.wxarticle.util.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {
	@ViewInject(R.id.username)
	private EditText username;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.inputcode)
	private EditText inputcode;
	@ViewInject(R.id.verify)
	private TextView verify;
	@ViewInject(R.id.register)
	private TextView register;
	private int currentsec = 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ViewUtils.inject(this);
		initToolBar(getString(R.string.register));
		register.setOnClickListener(this);
		verify.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		String phone = username.getText().toString().trim();
		String psw = password.getText().toString().trim();
		String code = inputcode.getText().toString().trim();
		switch (v.getId()) {
		case R.id.register:
			if (!Utils.isMobileNO(phone)) {
				showShortToast(R.string.regist_rightphone);
			} else if (psw.length() < 6 || psw.length() > 20) {
				showShortToast(R.string.regist_rightpwd);
			} else if (code.length() != 6) {
				showShortToast(R.string.regist_rightcode);
			} else {
				final MyUser user = new MyUser();
				user.setPassword(psw);
				user.setMobilePhoneNumber(phone);
				user.setUsername(phone);
				user.setCollect(new BmobRelation());
				user.setCollect_showapi(new BmobRelation());
				user.setComment(new BmobRelation());
				user.signOrLogin(this, code, new SaveListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						showLongToast(R.string.regist_success);
						finish();
					}

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						showLongToast(msg);
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
						if (ex == null) {
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
