package com.hereafter.wxarticle.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hereafter.wxarticle.DetailActivity;
import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.adapter.MainAdapter;
import com.hereafter.wxarticle.bmob.Juhe;
import com.hereafter.wxarticle.dao.JuheDao;
import com.hereafter.wxarticle.interf.RecyItemClickListener;
import com.hereafter.wxarticle.util.BmobUtil;
import com.hereafter.wxarticle.util.LogUtil;
import com.hereafter.wxarticle.util.RefreshLayout;
import com.hereafter.wxarticle.util.RefreshLayout.OnLoadListener;
import com.hereafter.wxarticle.widget.PullLoadMoreRecyclerView;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ContentFragment extends Fragment  {

	private PullLoadMoreRecyclerView mListView;

	private MainAdapter mAdapter;
	private ArrayList<Juhe> dataArr = new ArrayList<Juhe>();
	private JuheDao mdao;
	private int curpage = 1;
	private int totalPage = 0;
	private boolean isFirstShow = true;
	private Activity activity;
	private View root;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
		mdao = JuheDao.getInstance(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_content, null);
		mListView = (PullLoadMoreRecyclerView) root.findViewById(R.id.pullLoadMoreRecyclerView);
		mListView.setLinearLayout();
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		dataArr = mdao.queryAll();
		mAdapter = new MainAdapter(activity, dataArr);
		mAdapter.setRecyItemClickListener(new RecyItemClickListener() {
			@Override
			public void onItemClick(RecyclerView.ViewHolder holder, int position) {
				Juhe juhe = dataArr.get(position);
				Intent mIntent = new Intent(activity, DetailActivity.class);
				mIntent.putExtra("juhe", juhe);
				startActivity(mIntent);
			}
		});
		mListView.setAdapter(mAdapter);
		if (dataArr.size() != 0) {
			mAdapter.addData(dataArr);
			showRefresh();
		} else {
			showRefresh();
		}
		mListView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
			@Override
			public void onRefresh() {
				showRefresh();
			}

			@Override
			public void onLoadMore() {
				if (totalPage != 0 && curpage == totalPage) {// 判断是否到底了
					Toast.makeText(activity, R.string.already_nodata, Toast.LENGTH_SHORT).show();
				} else {
					curpage++;
					requestData();
				}
			}
		});
	}

	public void showRefresh() {
		requestData();
	}

	private void requestData() {
		Parameters params = new Parameters();
		params.add("pno", curpage);
		params.add("ps", 20);
		JuheData.executeWithAPI(activity, 147, "http://v.juhe.cn/weixin/query", JuheData.GET, params,
				new DataCallBack() {

					@Override
					public void onSuccess(int statusCode, String responseString) {
						// TODO Auto-generated method stub
						LogUtil.e(responseString);
						try {
							JSONObject json = new JSONObject(responseString);
							if (json.getString("reason").equals(getResources().getString(R.string.reason_success))) {
								json = json.getJSONObject("result");
								totalPage = json.getInt("totalPage");
								curpage = json.getInt("pno");
								 ArrayList<Juhe> dataBmobs = new ArrayList<Juhe>();
								JSONArray array = json.getJSONArray("list");
								for (int i = 0; i < array.length(); i++) {
									JSONObject object = array.getJSONObject(i);
									String id = object.getString("id");
									String title = object.getString("title");
									String source = object.getString("source");
									String firstImg = object.getString("firstImg");
									String mark = object.getString("mark");
									String url = object.getString("url");
									Juhe juhe = new Juhe(id, title, source, firstImg, mark, url);
									dataBmobs.add(juhe);
								}
								if (curpage==1){
									mAdapter.clearData();
								}
								mAdapter.addData(dataBmobs);
								mAdapter.notifyDataSetChanged();
								mListView.setPullLoadMoreCompleted();
								saveData(dataBmobs);
								if (totalPage == curpage) {
									Toast.makeText(activity, getString(R.string.already_nodata), Toast.LENGTH_SHORT)
											.show();
								}

							}else{
								Toast.makeText(activity, json.getString("reason"), Toast.LENGTH_SHORT)
										.show();
								mListView.setPullLoadMoreCompleted();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(int statusCode, String responseString, Throwable throwable) {
						// TODO Auto-generated method stub
						LogUtil.e("statusCode=" + statusCode + ",responseString=" + responseString);
					}
				});
	}

	public void saveData(final ArrayList<Juhe> dataBmobs) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				BmobUtil.updateJuheDataToBmob(activity, dataBmobs);
				mdao.insertAll(dataArr);
			}
		}).start();

	}

}