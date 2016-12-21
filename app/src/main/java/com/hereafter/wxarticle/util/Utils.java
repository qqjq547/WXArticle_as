package com.hereafter.wxarticle.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Environment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobDate;

public class Utils {
	private Bitmap createCircleImage(Bitmap source, int min) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("1\\d{10}");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isEmail(String email) {
		String str = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static File saveMyBitmap(Bitmap mBitmap, String bitName) throws IOException {
		File f = new File(Environment.getExternalStorageDirectory() + "/wxarticle/" + bitName + ".png");
		File dirFile = new File(Environment.getExternalStorageDirectory() + "/wxarticle/");
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public static int getStatusBarHeight(Context context) {
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}

	public static BmobDate formetToBmobDate(String datestr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = format.parse(datestr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new BmobDate(date);
	}

	public static String getVersionName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		String version;
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static void openAppInMarket(Context context) {
		if (context != null) {
			String pckName = context.getPackageName();
			try {
				gotoMarket(context, pckName);
			} catch (Exception ex) {
				try {
					String otherMarketUri = "http://market.android.com/details?id=" + pckName;
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(otherMarketUri));
					context.startActivity(intent);
				} catch (Exception e) {

				}
			}
		}
	}

	public static void gotoMarket(Context context, String pck) {
		if (!isHaveMarket(context)) {
			Toast.makeText(context, "你手机中没有安装应用市场！", Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + pck));
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivity(intent);
		}
	}

	public static boolean isHaveMarket(Context context) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.APP_MARKET");
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
		return infos.size() > 0;
	}
	public static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getDateStr(String formatdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		SimpleDateFormat sdf_today = new SimpleDateFormat("hh:mm");
		SimpleDateFormat sdf_before = new SimpleDateFormat("yy-MM-dd");
		try {
			Date date = sdf.parse(formatdate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			Calendar curcal = Calendar.getInstance();
			int d = curcal.get(Calendar.DATE) - cal.get(Calendar.DATE);
			if (d == 0) {
				return sdf_today.format(date);
			} else if (d == 1) {
				return "昨天";
			} else if (d == 2) {
				return "前天";
			} else {
				return sdf_before.format(date);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static void setWebViewTextFont(WebView mWebView){
		int textfontsize=PreferenceUtil.getInstance(mWebView.getContext()).getInt(PreferenceUtil.TEXT_FONT_SIZE,3);
		switch (textfontsize){
			case 1:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
				break;
			case 2:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
				break;
			case 3:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
				break;
			case 4:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
				break;
			case 5:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
				break;
		}
	}
}
