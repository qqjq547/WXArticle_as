package com.hereafter.wxarticle;

import java.util.Timer;
import java.util.TimerTask;

import com.hereafter.wxarticle.app.MyApplication;
import com.hereafter.wxarticle.bmob.MyUser;
import com.hereafter.wxarticle.fragment.ContentFragment;
import com.hereafter.wxarticle.fragment.LeftMenuFragment;
import com.hereafter.wxarticle.fragment.ShowRecyFragment;
import com.hereafter.wxarticle.fragment.ShowTypeFragment;
import com.hereafter.wxarticle.util.LogUtil;
import com.hereafter.wxarticle.util.PreferenceUtil;
import com.hereafter.wxarticle.util.ShareUtil;
import com.hereafter.wxarticle.util.Utils;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import cn.bmob.v3.update.BmobUpdateAgent;

public class MainActivity extends AppCompatActivity {

	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private Toolbar mToolbar;
	private LeftMenuFragment mLeftMenuFragment;
	private String mTitle;
	private static final String TAG = "MainActivity";
	private static final String KEY_TITLLE = "key_title";
	private ShareUtil share;
	private Fragment[] fragments = new Fragment[3];
	private int curposition = 0;
	private FragmentManager fm;
	private String[] titleArr;
	private static boolean isExit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		initToolBar();
		initViews();
		BmobUpdateAgent.initAppVersion(this);
		UmengUpdateAgent.update(this);
		share = new ShareUtil(this);
		titleArr = getResources().getStringArray(R.array.array_left_menu);
		fragments[0] = new ContentFragment();
		fragments[1] = new ShowRecyFragment();
		fragments[2] = new ShowTypeFragment();
		fm = getSupportFragmentManager();
		fm.beginTransaction().add(R.id.container, fragments[curposition]).commit();
		mToolbar.setTitle(titleArr[0]);
		mLeftMenuFragment = (LeftMenuFragment) fm.findFragmentById(R.id.id_left_menu_container);
		if (mLeftMenuFragment == null) {
			mLeftMenuFragment = new LeftMenuFragment();
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.replace(R.id.id_left_menu_container, mLeftMenuFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		};

		// 设置MenuItem的选择回调
		mLeftMenuFragment.setOnMenuItemSelectedListener(new LeftMenuFragment.OnMenuItemSelectedListener() {
			@Override
			public void menuItemSelected(int position) {
				if (position > fragments.length - 1) {
					if (position == 3) {
						startActivity(new Intent(MainActivity.this, CollectActivity.class));
					}
				} else {
					mToolbar.setTitle(titleArr[position]);
					switchContent(fragments[curposition], fragments[position]);
					mDrawerLayout.closeDrawer(Gravity.LEFT);
					curposition = position;
				}
			}
		});
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
		agent.openAudioFeedback();
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		mPushAgent.enable(new IUmengRegisterCallback() {

			@Override
			public void onRegistered(String registrationId) {
				//onRegistered方法的参数registrationId即是device_token
				LogUtil.d("device_token="+registrationId);
			}
		});
		PushAgent.getInstance(this).onAppStart();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_TITLLE, mTitle);
	}

	private void initToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
		mToolbar.setTitle(getResources().getStringArray(R.array.array_left_menu)[0]);
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			int statusBarHeight = Utils.getStatusBarHeight(this);
			mToolbar.setPadding(0, statusBarHeight, 0, 0);
		}
		setSupportActionBar(mToolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void initViews() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open,
				R.string.close);

		mActionBarDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		PgyFeedbackShakeManager.register(this);
	}

	@Override
	public void onPause() {
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		share.onActivityResult(requestCode, resultCode, data);
	}

	public void switchContent(Fragment from, Fragment to) {
		if (from != to) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			if (!to.isAdded()) {
				transaction.hide(from).add(R.id.container, to).commit();
			} else {
				transaction.hide(from).show(to).commit();
			}
		}
	}

	// public void getCloud(){
	// String cloudCodeName = "getJuheCollectByUser";
	// JSONObject params = new JSONObject();
	// //name是上传到云端的参数名称，值是bmob，云端代码可以通过调用request.body.name获取这个值
	// try {
	// params.put("juheId", "85f02dd177");
	// params.put("username", MyUser.getCurrentUser(this,
	// MyUser.class).getUsername());
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// //创建云端代码对象
	// AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
	// //异步调用云端代码
	// cloudCode.callEndpoint(MainActivity.this, cloudCodeName, params, new
	// CloudCodeListener() {
	//
	// //执行成功时调用，返回result对象
	// @Override
	// public void onSuccess(Object result) {
	// Log.i("hjq", "getCloud:result = "+result.toString());
	// }
	//
	// @Override
	// public void onFailure(int arg0, String arg1) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		LogUtil.i("onBackPressed");
		PreferenceUtil pref = PreferenceUtil.getInstance(this);
		if (pref.isDoubleClick()) {
			doubleClickExit();
		} else {
			AlertDialog.Builder buildr = new AlertDialog.Builder(this);
			buildr.setMessage(R.string.main_exit_makesure).setNegativeButton(R.string.sure, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					MyApplication.getInstance().finishActivity();
				}
			}).setPositiveButton(R.string.cancel, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			}).create().show();
		}
	}

	public void openShare() {
		share.show();
	}

	private void doubleClickExit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(this, R.string.main_exit_again, Toast.LENGTH_SHORT).show();
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			MyApplication.getInstance().finishActivity();
		}
	}

}
