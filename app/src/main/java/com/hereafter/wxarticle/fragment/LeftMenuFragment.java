package com.hereafter.wxarticle.fragment;

import com.hereafter.wxarticle.LoginActivity;
import com.hereafter.wxarticle.MainActivity;
import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.SettingActivity;
import com.hereafter.wxarticle.UserActivity;
import com.hereafter.wxarticle.adapter.LeftMenuAdapter;
import com.hereafter.wxarticle.bmob.MyUser;
import com.hereafter.wxarticle.model.MenuItem;
import com.hereafter.wxarticle.util.ImageLoaderUtil;
import com.hereafter.wxarticle.widget.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

public class LeftMenuFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

	private MenuItem[] mItems = null;
	private View root;
	private ListView listView;
	private CircleImageView headimg;
	private TextView username;
	private LinearLayout settinglin;
	private LinearLayout sharelin;
	private LeftMenuAdapter mAdapter;
	private Activity activity;
	private MyUser curUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_left, null);
		listView = (ListView) root.findViewById(R.id.list);
		headimg = (CircleImageView) root.findViewById(R.id.head);
		username = (TextView) root.findViewById(R.id.username);
		settinglin = (LinearLayout) root.findViewById(R.id.setting);
		sharelin = (LinearLayout) root.findViewById(R.id.share);
		settinglin.setOnClickListener(this);
		sharelin.setOnClickListener(this);
		return root;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		MenuItem menuItem = null;
		String[] strarr = getResources().getStringArray(R.array.array_left_menu);
		mItems = new MenuItem[strarr.length];
		for (int i = 0; i < strarr.length; i++) {
			menuItem = new MenuItem(strarr[i], false);
			mItems[i] = menuItem;
		}
		headimg.setOnClickListener(this);
		mAdapter = new LeftMenuAdapter(getActivity(), mItems);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		curUser = BmobUser.getCurrentUser(activity, MyUser.class);
		if (curUser != null) {
			if (!TextUtils.isEmpty(curUser.getHeadimg())) {
				ImageLoaderUtil.displayImage(curUser.getHeadimg(), headimg, activity);
			}
			if (TextUtils.isEmpty(curUser.getNickname())) {
				username.setText(curUser.getUsername());
			} else {
				username.setText(curUser.getNickname());
			}
		}
	}

	// 选择回调的接口
	public interface OnMenuItemSelectedListener {
		void menuItemSelected(int position);
	}

	private OnMenuItemSelectedListener mMenuItemSelectedListener;

	public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener menuItemSelectedListener) {
		this.mMenuItemSelectedListener = menuItemSelectedListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.head:
			BmobUser curuser = BmobUser.getCurrentUser(activity);
			if (curuser == null) {
				startActivity(new Intent(activity, LoginActivity.class));
			} else {
				startActivity(new Intent(activity, UserActivity.class));
			}
			break;
		case R.id.setting:
			startActivity(new Intent(activity, SettingActivity.class));
			break;
		case R.id.share:
			((MainActivity) activity).openShare();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (mMenuItemSelectedListener != null) {
			mMenuItemSelectedListener.menuItemSelected(position);
		}
		mAdapter.setSelected(position);
	}
}