package com.hereafter.wxarticle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hereafter.wxarticle.adapter.CommentAdapter;
import com.hereafter.wxarticle.bmob.Comment;
import com.hereafter.wxarticle.bmob.Juhe;
import com.hereafter.wxarticle.bmob.MyUser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class CommentActivity extends BaseActivity {
	@ViewInject(R.id.listview)
	private ListView listview;
	@ViewInject(R.id.edittext)
	private EditText edittext;
	@ViewInject(R.id.sendimg)
	private ImageView sendimg;
	private ArrayList<Comment> commentArr = new ArrayList<Comment>();
	private CommentAdapter mAdapter;
	private MyUser user;
	private Juhe juhe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commentlist);
		ViewUtils.inject(this);
		initToolBar(getString(R.string.commentlist_title));
		juhe = (Juhe) getIntent().getSerializableExtra("juhe");
		user = BmobUser.getCurrentUser(this, MyUser.class);
		sendimg.setOnClickListener(this);
		mAdapter = new CommentAdapter(CommentActivity.this, commentArr);
		listview.setAdapter(mAdapter);
		getListData();
	}

	private void getListData() {
		juhe.getComments(this, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> arg0) {
				// TODO Auto-generated method stub
				if (arg0.size() > 0) {
					commentArr.clear();
					commentArr.addAll(arg0);
					mAdapter.notifyDataSetChanged();
				} else {
					showShortToast(R.string.comment_nofinddata);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showShortToast(R.string.comment_getdatafailed);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.sendimg:
			if (user == null) {
				startActivity(new Intent(this, LoginActivity.class));
			} else {
				String content = edittext.getText().toString().trim();
				if (TextUtils.isEmpty(content)) {
					showShortToast(R.string.comment_edittext_hint);
				} else {
					final Comment comment = new Comment(juhe.getObjectId(), content, new BmobDate(new Date()), user);
					comment.save(this, new SaveListener() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							BmobRelation relation = new BmobRelation();
							relation.add(comment);
							juhe.setComment(relation);
							juhe.update(CommentActivity.this, new UpdateListener() {

								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									try {
										showShortToast(R.string.comment_success);
										sendimg.setClickable(true);
										edittext.setText("");
										getListData();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

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
}
