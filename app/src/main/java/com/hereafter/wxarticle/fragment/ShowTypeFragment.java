package com.hereafter.wxarticle.fragment;

import java.util.HashMap;
import java.util.Map;

import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.widget.PagerSlidingTabStrip;
import com.squareup.okhttp.OkHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowTypeFragment extends Fragment implements OnItemClickListener {
	private View root;
	private Activity activity;
	private final OkHttpClient client = new OkHttpClient();
	private Map<Integer, String> typeMap = new HashMap<Integer, String>();
	private ViewPager mPager;
	private PagerSlidingTabStrip pagertab;
	private ShowTypeAdapter typeAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.fragment_type, null);
		mPager = (ViewPager) root.findViewById(R.id.viewpager);
		pagertab = (PagerSlidingTabStrip) root.findViewById(R.id.tabs);
		pagertab.setIndicatorColorResource(R.color.style_color_primary);

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (typeAdapter == null) {
			initTypeMap();
			typeAdapter = new ShowTypeAdapter(getFragmentManager(), typeMap);
			mPager.setAdapter(typeAdapter);
			pagertab.setViewPager(mPager);
		} else {
			typeAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		// ShowApi showapi=listdata.get(position);
		// Intent mIntent=new Intent(activity, WebActivity.class);
		// mIntent.putExtra("showapi", showapi);
		// startActivity(mIntent);

	}

	private void initTypeMap() {
		String[] titleArr = getResources().getStringArray(R.array.showapi_title_array);
		for (int i = 0; i < titleArr.length; i++) {
			typeMap.put(i + 1, titleArr[i]);
		}
	}

	class ShowTypeAdapter extends FragmentPagerAdapter {
		private Map<Integer, String> map = new HashMap<Integer, String>();
		private ShowFragment[] fragmentList = null;

		public ShowTypeAdapter(FragmentManager fm, Map<Integer, String> mapdata) {
			super(fm);
			// TODO Auto-generated constructor stub
			map = mapdata;
			fragmentList = new ShowFragment[map.size()];
			for (int i = 0; i < fragmentList.length; i++) {
				fragmentList[i] = new ShowFragment(i + 1);
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return map.get(position + 1);
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return fragmentList[position];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return map.size();
		}

	}
}
