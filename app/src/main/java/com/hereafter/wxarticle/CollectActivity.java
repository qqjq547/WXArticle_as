package com.hereafter.wxarticle;

import java.util.ArrayList;
import java.util.List;

import com.hereafter.wxarticle.adapter.MainAdapter;
import com.hereafter.wxarticle.adapter.ShowRecyAdapter;
import com.hereafter.wxarticle.bmob.Juhe;
import com.hereafter.wxarticle.bmob.MyUser;
import com.hereafter.wxarticle.bmob.ShowApi;
import com.hereafter.wxarticle.interf.RecyItemClickListener;
import com.hereafter.wxarticle.widget.PullLoadMoreRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.v3.listener.FindListener;

public class CollectActivity extends BaseActivity{

	@ViewInject(R.id.tablayout)
	private TabLayout tablayout;
	@ViewInject(R.id.pager)
	private ViewPager viewpager;
	private MainAdapter mMainAdapter;
	private ShowRecyAdapter mShowAdapter;
	private MyUser user;
	private ArrayList<Juhe> juheArr = new ArrayList<Juhe>();
	private ArrayList<ShowApi> showArr = new ArrayList<ShowApi>();
	private PullLoadMoreRecyclerView[] collectView = new PullLoadMoreRecyclerView[2];
	private String[] titleRes;
	private final int jxViewID = 0x100;
	private final int hotViewID = 0x200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
		ViewUtils.inject(this);
		initToolBar(getString(R.string.mycollect_title));
		titleRes = getResources().getStringArray(R.array.collect_title_array);
		TabLayout.Tab tab1 = tablayout.newTab().setText(titleRes[0]);
		TabLayout.Tab tab2 = tablayout.newTab().setText(titleRes[1]);
		tablayout.addTab(tab1);
		tablayout.addTab(tab2);
		user = MyUser.getCurrentUser(this, MyUser.class);
		if (user == null) {
			showShortToast(R.string.please_login);
		} else {
			initView();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		collectView[0] = (PullLoadMoreRecyclerView) LayoutInflater.from(this).inflate(R.layout.listview, null);
		collectView[1] = (PullLoadMoreRecyclerView) LayoutInflater.from(this).inflate(R.layout.listview, null);
		mMainAdapter = new MainAdapter(this, juheArr);
		mMainAdapter.setRecyItemClickListener(new RecyItemClickListener() {
			@Override
			public void onItemClick(RecyclerView.ViewHolder holder, int position) {
				Juhe juhe = juheArr.get(position);
				Intent mIntent = new Intent(CollectActivity.this, DetailActivity.class);
				mIntent.putExtra("juhe", juhe);
				startActivity(mIntent);
			}
		});
		collectView[0].setAdapter(mMainAdapter);
		mShowAdapter = new ShowRecyAdapter(this, showArr);
		mShowAdapter.setRecyItemClickListener(new RecyItemClickListener() {
			@Override
			public void onItemClick(RecyclerView.ViewHolder holder, int position) {
				ShowApi show = showArr.get(position);
				Intent mIntent = new Intent(CollectActivity.this, ShowApiActivity.class);
				mIntent.putExtra("showapi", show);
				startActivity(mIntent);
			}
		});
		collectView[1].setAdapter(mShowAdapter);
		viewpager.setAdapter(new CollectAdapter());
		tablayout.setupWithViewPager(viewpager);
		getListData();
	}

	private void getListData() {
		user.getCollectJuheByUser(this, new FindListener<Juhe>() {

			@Override
			public void onSuccess(List<Juhe> arg0) {
				// TODO Auto-generated method stub
				juheArr.addAll(arg0);
				mMainAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
		user.getCollectShowApiByUser(this, new FindListener<ShowApi>() {

			@Override
			public void onSuccess(List<ShowApi> arg0) {
				// TODO Auto-generated method stub
				showArr.addAll(arg0);
				mShowAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		default:
			break;
		}
	}

	public class CollectAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return collectView.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(collectView[position]);
			return collectView[position];
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titleRes[position];
		}
	}
}
