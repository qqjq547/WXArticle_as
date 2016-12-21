package com.hereafter.wxarticle;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hereafter.wxarticle.bmob.Comment;
import com.hereafter.wxarticle.bmob.MyUser;
import com.hereafter.wxarticle.bmob.ShowApi;
import com.hereafter.wxarticle.bmob.ShowApi;
import com.hereafter.wxarticle.util.LogUtil;
import com.hereafter.wxarticle.util.PreferenceUtil;
import com.hereafter.wxarticle.util.ShareUtil;
import com.hereafter.wxarticle.util.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ShowApiActivity extends BaseActivity {
	@ViewInject(R.id.web)
	private WebView mWebView;
	@ViewInject(R.id.detail_share)
	private LinearLayout sharelin;
	@ViewInject(R.id.detail_collection)
	private LinearLayout collectLin;
	@ViewInject(R.id.detail_like)
	private LinearLayout likeLin;
	@ViewInject(R.id.detail_comment)
	private LinearLayout commentLin;
	@ViewInject(R.id.icon_collection)
	private ImageView collectImg;
	@ViewInject(R.id.icon_like)
	private ImageView likeImg;
	@ViewInject(R.id.text_collect)
	private TextView collectText;
	@ViewInject(R.id.text_like)
	private TextView likeText;
	@ViewInject(R.id.text_commemt)
	private TextView text_commemt;
	@ViewInject(R.id.operateboard)
	private LinearLayout operateboard;
	@ViewInject(R.id.editboard)
	private LinearLayout editboard;
	@ViewInject(R.id.showedit)
	private ImageView showedit;
	@ViewInject(R.id.showoperate)
	private ImageView showoperate;
	@ViewInject(R.id.edittext)
	private EditText edittext;
	@ViewInject(R.id.sendimg)
	private ImageView sendimg;
	@ViewInject(R.id.progress)
	private ProgressBar progress;
	private ShowApi mshow;
	private String title;
	private String url;
	private MyUser user;
	private TranslateAnimation dismissAnim;
	private TranslateAnimation showAnim;
	private boolean isCollect = false;
	private int likenum = 0;
	private ShareUtil share;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		ViewUtils.inject(this);
		mshow = (ShowApi) getIntent().getSerializableExtra("showapi");
		title = mshow.getTitle();
		url = mshow.getUrl();
		initToolBar(title);
		LogUtil.d("url=" + url);
		user = (MyUser) BmobUser.getCurrentUser(this, MyUser.class);
		mWebView.setBackgroundColor(0);
		mWebView.setBackgroundResource(R.color.white);
		mWebView.getSettings().setLoadsImagesAutomatically(true);
		mWebView.getSettings().setDefaultTextEncodingName("utf-8");
		mWebView.getSettings().setJavaScriptEnabled(true);
		Utils.setWebViewTextFont(mWebView);
		mWebView.setWebViewClient(new webViewClient());
		mWebView.setWebChromeClient(new ChromeClient());
		mWebView.setDownloadListener(new MyWebViewDownLoadListener());
		mWebView.loadUrl(url);
		Map<String, String> map_value = new HashMap<String, String>();
		map_value.put("title", title);
		map_value.put("url", url);
		MobclickAgent.onEvent(this, "opendetail", map_value);
		if (user != null) {
			user.getObjectId();
			user.getCreatedAt();
		}
		sharelin.setOnClickListener(this);
		collectLin.setOnClickListener(this);
		likeLin.setOnClickListener(this);
		commentLin.setOnClickListener(this);
		showedit.setOnClickListener(this);
		showoperate.setOnClickListener(this);
		sendimg.setOnClickListener(this);
		if (mshow != null) {
			InitShowApi();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.actionmenu, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (share != null) {
			share.onActivityResult(arg0, arg1, arg2);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
			case R.id.textfont_minsmall:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
				PreferenceUtil.getInstance(this).setInt(PreferenceUtil.TEXT_FONT_SIZE,1);
				return true;
			case R.id.textfont_small:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
				PreferenceUtil.getInstance(this).setInt(PreferenceUtil.TEXT_FONT_SIZE,2);
				return true;
			case R.id.textfont_normal:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
				PreferenceUtil.getInstance(this).setInt(PreferenceUtil.TEXT_FONT_SIZE,3);
				return true;
			case R.id.textfont_large:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
				PreferenceUtil.getInstance(this).setInt(PreferenceUtil.TEXT_FONT_SIZE,4);
				return true;
			case R.id.textfont_maxlarge:
				mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
				PreferenceUtil.getInstance(this).setInt(PreferenceUtil.TEXT_FONT_SIZE,5);
				return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class ChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			// TODO Auto-generated method stub
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			if (newProgress == 100) {
				progress.setVisibility(View.INVISIBLE);
			} else {
				if (progress.getVisibility() == View.INVISIBLE)
					progress.setVisibility(View.VISIBLE);
				progress.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	class webViewClient extends WebViewClient {
		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

	}

	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
				long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.detail_share:
			share = new ShareUtil(this, mshow.getTitle(), mshow.getUrl());
			share.show();
			break;
		case R.id.detail_collection:
			Collect();
			break;
		case R.id.detail_like:
			like();
			break;
		case R.id.detail_comment:
			if (mshow != null) {
				Intent intent = new Intent(this, ShowApiCommentActivity.class);
				intent.putExtra("showapi", mshow);
				startActivity(intent);
			} else {
				showShortToast(R.string.detail_nofindcollectmsg);
			}
			break;
		case R.id.showedit:
			dismissBoard(operateboard, editboard);
			break;
		case R.id.showoperate:
			dismissBoard(editboard, operateboard);
			break;
		case R.id.sendimg:
			String content = edittext.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				showShortToast(R.string.comment_edittext_hint);
			} else {
				if (user == null) {
					startActivity(new Intent(this, LoginActivity.class));
				} else if (mshow == null) {
					showShortToast(R.string.detail_nofindmsg);
				} else {
					sendimg.setClickable(false);
					final Comment comment = new Comment(mshow.getObjectId(), content, new BmobDate(new Date()), user);
					comment.save(this, new SaveListener() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							BmobRelation relation = new BmobRelation();
							relation.add(comment);
							mshow.setComment(relation);
							mshow.update(ShowApiActivity.this, new UpdateListener() {

								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									showShortToast(R.string.comment_success);
									sendimg.setClickable(true);
									edittext.setText("");
									InitShowApi();

								}

								@Override
								public void onFailure(int arg0, String arg1) {
									// TODO Auto-generated method stub
									showShortToast(R.string.comment_failded);
									sendimg.setClickable(true);
								}
							});
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							showShortToast(R.string.comment_send_failded);
							sendimg.setClickable(true);
						}
					});
				}
			}
			break;
		default:
			break;
		}
	}

	private void InitShowApi() {
		BmobQuery<ShowApi> query = new BmobQuery<ShowApi>();
		query.addWhereEqualTo("id", mshow.getId());
		query.findObjects(this, new FindListener<ShowApi>() {
			@Override
			public void onSuccess(List<ShowApi> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null && arg0.size() > 0) {
					mshow = arg0.get(0);
					LogUtil.e(mshow.toString());
					mshow.getComments(ShowApiActivity.this, new FindListener<Comment>() {
						@Override
						public void onSuccess(List<Comment> object) {
							// TODO Auto-generated method stub
							LogUtil.d("getComments=" + object.size());
							text_commemt.setText(String.valueOf(object.size()));
						}

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub

						}
					});

					mshow.getCollectByData(ShowApiActivity.this, new FindListener<MyUser>() {

						@Override
						public void onSuccess(List<MyUser> object) {
							// TODO Auto-generated method stub
							for (MyUser myuser : object) {
								if (myuser.getObjectId().contains(user.getObjectId())) {
									isCollect = true;
									collectImg.setImageResource(R.drawable.collect_press);
								} else {
									collectImg.setImageResource(R.drawable.collect_normal);
								}
							}

						}

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub

						}
					});
					mshow.getLike(ShowApiActivity.this, new FindListener<MyUser>() {

						@Override
						public void onSuccess(List<MyUser> arg0) {
							// TODO Auto-generated method stub
							likenum = arg0.size();
							likeText.setText(likenum + "");
						}

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub

						}
					});
				}

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				InitShowApi();
			}
		});

	}

	public void showBoard(final View board) {
		showAnim = new TranslateAnimation(-operateboard.getWidth(), 0, 0, 0);
		showAnim.setDuration(1000);
		showAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				board.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

			}
		});
		board.startAnimation(showAnim);
	}

	public void dismissBoard(final View dismissboard, final View showboard) {
		dismissAnim = new TranslateAnimation(0, dismissboard.getWidth(), 0, 0);
		dismissAnim.setDuration(1000);
		dismissAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				dismissboard.setVisibility(View.INVISIBLE);
				showBoard(showboard);
			}
		});
		dismissboard.startAnimation(dismissAnim);
	}

	public void Collect() {
		if (user == null) {
			startActivity(new Intent(this, LoginActivity.class));
		} else {
			if (mshow == null) {
				showShortToast(R.string.detail_nofindcollectmsg);
			} else {
				mshow.getCollectByData(ShowApiActivity.this, new FindListener<MyUser>() {

					@Override
					public void onSuccess(List<MyUser> users) {
						// TODO Auto-generated method stub
						for (MyUser myuser : users) {
							if (myuser.getObjectId().equals(user.getObjectId())) {
								isCollect = true;
								break;
							}
						}

						user.addShowApiCollect(ShowApiActivity.this, mshow, new UpdateListener() {
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								mshow.addCollectData(ShowApiActivity.this, user, new UpdateListener() {
									@Override
									public void onSuccess() {
										// TODO Auto-generated method stub
										isCollect = !isCollect;
										if (isCollect) {
											showShortToast(R.string.detail_collectsuccess);
											collectImg.setImageResource(R.drawable.collect_press);
										} else {
											showShortToast(R.string.detail_quitcollectsuccess);
											collectImg.setImageResource(R.drawable.collect_normal);
										}

									}

									@Override
									public void onFailure(int arg0, String arg1) {
										// TODO Auto-generated method stub
										LogUtil.d("addCollectData="+arg1);
										showShortToast(R.string.detail_operate_fail);
									}
								});
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								LogUtil.d("addShowApiCollect="+arg1);
								showShortToast(R.string.detail_operate_fail);
							}
						});

					}

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtil.d("getCollectByData="+arg1);
						showShortToast(R.string.detail_operate_fail);
					}
				});

			}
		}
	}

	private void like() {
		// TODO Auto-generated method stub
		if (user == null) {
			startActivity(new Intent(this, LoginActivity.class));
		} else {
			if (mshow == null) {
			} else {
				if (user.islikeShowApi(mshow)) {
					showShortToast(R.string.detail_already_like);
				} else {
					mshow.addLikeData(this, user, new UpdateListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							likenum++;
							likeText.setText(String.valueOf(likenum));
							showShortToast(R.string.detail_like_success);
							InitShowApi();
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							showShortToast(R.string.detail_like_fail);
						}
					});
				}
			}
		}
	}

}
