package com.hereafter.wxarticle;

import com.hereafter.wxarticle.app.MyApplication;
import com.hereafter.wxarticle.bmob.MyUser;
import com.hereafter.wxarticle.util.PreferenceUtil;
import com.hereafter.wxarticle.util.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pgyersdk.activity.FeedbackActivity;
import com.pgyersdk.feedback.PgyFeedback;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingActivity extends BaseActivity {
	@ViewInject(R.id.rl_loading_img)
	private RelativeLayout rl_loading_img;
	@ViewInject(R.id.tb_loading_img)
	private ToggleButton tb_loading_img;
	@ViewInject(R.id.rl_update)
	private RelativeLayout rl_update;
	@ViewInject(R.id.curversion)
	private TextView curversion;
	@ViewInject(R.id.rl_advtise)
	private RelativeLayout rl_advtise;
	@ViewInject(R.id.rl_feedback)
	private RelativeLayout rl_feedback;
	@ViewInject(R.id.rl_double_click_exit)
	private RelativeLayout rl_double_click_exit;
	@ViewInject(R.id.tb_double_click_exit)
	private ToggleButton tb_double_click_exit;
	@ViewInject(R.id.rl_grade)
	private RelativeLayout rl_grade;
	@ViewInject(R.id.rl_exit)
	private RelativeLayout rl_exit;
	private PreferenceUtil pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ViewUtils.inject(this);
		initToolBar(getString(R.string.setting));
		pref = PreferenceUtil.getInstance(SettingActivity.this);
		curversion.setText(Utils.getVersionName(this));
		rl_loading_img.setOnClickListener(this);
		rl_update.setOnClickListener(this);
		rl_advtise.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
		rl_double_click_exit.setOnClickListener(this);
		rl_grade.setOnClickListener(this);
		rl_exit.setOnClickListener(this);
		tb_loading_img.setChecked(pref.isLoadImg());
		tb_loading_img.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				pref.setLoadImg(isChecked);
			}
		});
		tb_double_click_exit.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				pref.setDoubleExit(isChecked);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_loading_img:
			if (tb_loading_img.isChecked()) {
				tb_loading_img.setChecked(false);
			} else {
				tb_loading_img.setChecked(true);
			}
			break;
		case R.id.rl_update:
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					// TODO Auto-generated method stub
					switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
						break;
					case UpdateStatus.No: // has no update
						Toast.makeText(SettingActivity.this, "已经是最新的版本啦！", Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.NoneWifi: // none wifi
						Toast.makeText(SettingActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.Timeout: // time out
						Toast.makeText(SettingActivity.this, "请求超时", Toast.LENGTH_SHORT).show();
						break;
					}
				}
			});
			UmengUpdateAgent.update(this);
			break;
		case R.id.rl_advtise:
			new FeedbackAgent(this).startFeedbackActivity();
			break;
		case R.id.rl_feedback:
			 FeedbackActivity.setBarImmersive(true);
			 PgyFeedback.getInstance().showActiivty(SettingActivity.this);
			break;
		case R.id.rl_double_click_exit:
			if (tb_double_click_exit.isChecked()) {
				tb_double_click_exit.setChecked(false);
			} else {
				tb_double_click_exit.setChecked(true);
			}
			break;
		case R.id.rl_grade:
			Utils.gotoMarket(this, getPackageName());
			break;
		case R.id.rl_exit:
			onClickExit();
			break;
		default:
			break;
		}
	}

	private void onClickExit() {
		final MyUser user = MyUser.getCurrentUser(this, MyUser.class);
		final Dialog dialog = new Dialog(this, R.style.DialogLight);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_setting_exit, null);
		view.findViewById(R.id.rl_app_exit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				MyApplication.getInstance().finishActivity();
			}
		});
		view.findViewById(R.id.rl_loginout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				user.logOut(SettingActivity.this);
			}
		});
		if (user == null) {
			view.findViewById(R.id.rl_loginout).setVisibility(View.GONE);
			view.findViewById(R.id.v_line).setVisibility(View.GONE);
		}
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.show();
	}
}
