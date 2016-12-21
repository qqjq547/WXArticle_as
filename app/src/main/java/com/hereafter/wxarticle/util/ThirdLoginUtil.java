package com.hereafter.wxarticle.util;

import java.util.Map;

import org.json.JSONObject;

import com.hereafter.wxarticle.MainActivity;
import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.bmob.MyUser;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobUser.BmobThirdUserAuth;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.UpdateListener;

public class ThirdLoginUtil {
	private Activity activity;
	private UMShareAPI mShareAPI;

	public ThirdLoginUtil(Activity activity) {
		mShareAPI = UMShareAPI.get(activity);
		this.activity = activity;
	}

	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(final SHARE_MEDIA platform, int action, Map<String, String> data) {
			String token = data.get("access_token");
			String expires = data.get("expires_in");
			String openId = "";
			if (platform == SHARE_MEDIA.SINA) {
				openId = data.get("uid");
			} else {
				openId = data.get("openid");
			}
			String type = "";
			switch (platform) {
			case SINA:
				type = BmobThirdUserAuth.SNS_TYPE_WEIBO;
				break;
			case WEIXIN:
				type = BmobThirdUserAuth.SNS_TYPE_WEIXIN;
				break;
			case QQ:
				type = BmobThirdUserAuth.SNS_TYPE_QQ;
				break;
			default:
				break;
			}
			final BmobThirdUserAuth authInfo = new BmobThirdUserAuth(type, token, expires, openId);
			BmobUser.loginWithAuthData(activity, authInfo, new OtherLoginListener() {

				@Override
				public void onSuccess(JSONObject userAuth) {
					// TODO Auto-generated method stub
					final MyUser user = MyUser.getCurrentUser(activity, MyUser.class);
					mShareAPI.getPlatformInfo(activity, platform, new UMAuthListener() {

						@Override
						public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> map) {
							// TODO Auto-generated method stub
							for (Map.Entry<String, String> entry : map.entrySet()) {
								LogUtil.i("Key = " + entry.getKey() + ", Value = " + entry.getValue());
							}
							String headImgUrl = map.get("profile_image_url");
							String city = map.get("city");
							String nickname = map.get("screen_name");
							user.setHeadimg(headImgUrl);
							user.setCity(city);
							user.setNickname(nickname);
							user.update(activity, new UpdateListener() {
								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									Intent intent = new Intent(activity, MainActivity.class);
									activity.startActivity(intent);
									activity.finish();
								}

								@Override
								public void onFailure(int arg0, String arg1) {
									// TODO Auto-generated method stub

								}
							});

						}

						@Override
						public void onCancel(SHARE_MEDIA arg0, int arg1) {
							// TODO Auto-generated method stub

						}
					});

				}

				@Override
				public void onFailure(int code, String msg) {
					// TODO Auto-generated method stub
					LogUtil.e("第三方登陆失败：" + msg);
				}

			});
			Toast.makeText(activity, activity.getString(R.string.thirdlogin_auth_success), Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText(activity, activity.getString(R.string.thirdlogin_auth_fail), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText(activity, activity.getString(R.string.thirdlogin_auth_cancel), Toast.LENGTH_SHORT).show();
		}
	};

	public void showQQAuth() {
		mShareAPI.doOauthVerify(activity, SHARE_MEDIA.QQ, umAuthListener);
	}

	public void showSinaAuth() {
		mShareAPI.doOauthVerify(activity, SHARE_MEDIA.SINA, umAuthListener);
	}

	public void showWeChatAuth() {

		mShareAPI.doOauthVerify(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/** 使用SSO授权必须添加如下代码 */
		mShareAPI.onActivityResult(requestCode, resultCode, data);
	}
}
