package com.hereafter.wxarticle.app;

import java.util.LinkedList;

import com.hereafter.wxarticle.dao.ShowApiDao;
import com.hereafter.wxarticle.util.ImageLoaderUtil;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

	private static MyApplication instance;
	private static LinkedList<Activity> activityList;
    private PushAgent mPushAgent;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		instance = this;
		super.onCreate();
		activityList = new LinkedList<Activity>();
		PlatformConfig.setWeixin("wxd61c4bdc2060e7cb", "a9381049647f55617ee0a626ac439cf6");
		// 微信 appid appsecret
		PlatformConfig.setSinaWeibo("4189290142", "bbb7bc245f03e6b4b9683a4cb0732cad");
		// 新浪微博 appkey appsecret
		PlatformConfig.setQQZone("1104972371", "h4Bc1aY27jrXiXhN");
		// QQ和Qzone appid appkey
		ShowApiDao.getInstance(this);
		mPushAgent=PushAgent.getInstance(this);
		mPushAgent.setDebugMode(true);
		mPushAgent.setMessageHandler(new UmengMessageHandler() {
			@Override
			public void dealWithNotificationMessage(Context arg0, UMessage msg) {
				// 调用父类方法,这里会在通知栏弹出提示信息
				super.dealWithNotificationMessage(arg0, msg);
				Log.e("", "### 自行处理推送消息");
			}
		});
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
		    @Override
		    public void launchApp(Context arg0, UMessage arg1) {
		    	// TODO Auto-generated method stub
		    	super.launchApp(arg0, arg1);
		    }
		    @Override
		    public void openUrl(Context arg0, UMessage arg1) {
		    	// TODO Auto-generated method stub
		    	super.openUrl(arg0, arg1);
		    }
		    @Override
		    public void openActivity(Context arg0, UMessage arg1) {
		    	// TODO Auto-generated method stub
		    	super.openActivity(arg0, arg1);
		    }
			@Override
		    public void dealWithCustomAction(Context context, UMessage msg) {
		        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
		    }
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
		try {
			mPushAgent.getTagManager().add("UMENG_CHANNEL");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PgyCrashManager.register(this);
	}

	public static MyApplication getInstance() {
		return instance;
	}

	/**
	 * Activity关闭时，删除Activity列表中的Activity对象
	 */
	public void removeActivity(Activity a) {
		activityList.remove(a);
	}

	/**
	 * 向Activity列表中添加Activity对象
	 */
	public void addActivity(Activity a) {
		activityList.add(a);
	}

	/**
	 * 关闭Activity列表中的所有Activity
	 */
	public void finishActivity() {
		for (Activity activity : activityList) {
			if (null != activity) {
				activity.finish();
			}
		}
		activityList.clear();
		// 杀死该应用进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		ImageLoaderUtil.stopload(instance);
		super.onTerminate();
	}
}
