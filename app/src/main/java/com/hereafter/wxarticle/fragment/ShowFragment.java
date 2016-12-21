package com.hereafter.wxarticle.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.ShowApiActivity;
import com.hereafter.wxarticle.adapter.RecyAdapter;
import com.hereafter.wxarticle.adapter.ShowRecyAdapter;
import com.hereafter.wxarticle.bmob.ShowApi;
import com.hereafter.wxarticle.dao.ShowApiDao;
import com.hereafter.wxarticle.http.HttpUtil;
import com.hereafter.wxarticle.http.ResponeCallback;
import com.hereafter.wxarticle.interf.RecyItemClickListener;
import com.hereafter.wxarticle.util.BmobUtil;
import com.hereafter.wxarticle.util.RefreshLayout;
import com.hereafter.wxarticle.util.RefreshLayout.OnLoadListener;
import com.hereafter.wxarticle.widget.PullLoadMoreRecyclerView;
import com.squareup.okhttp.OkHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ShowFragment extends Fragment implements OnItemClickListener {
	private View root;
	private PullLoadMoreRecyclerView listview;
	private Activity activity;
	private final OkHttpClient client = new OkHttpClient();
	private int pageNum = 1;
	private int allPages = 0;
	private ArrayList<ShowApi> listdata = new ArrayList<ShowApi>();
	private ShowRecyAdapter mAdapter;
	private int id = 0;
	private ShowApiDao dao;

	public ShowFragment(int id) {
		super();
		this.id = id;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
		dao = ShowApiDao.getInstance(activity);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.fragment_showlist, null);
		listview = (PullLoadMoreRecyclerView) root.findViewById(R.id.pullLoadMoreRecyclerView);
		listview.setLinearLayout();
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.e("hjq", "onActivityCreated");
		listdata = dao.queryAll();
		mAdapter = new ShowRecyAdapter(activity,listdata);
		mAdapter.setRecyItemClickListener(new RecyItemClickListener() {
			@Override
			public void onItemClick(RecyclerView.ViewHolder holder, int position) {
				mAdapter.setRecyItemClickListener(new RecyItemClickListener() {
					@Override
					public void onItemClick(RecyclerView.ViewHolder holder, int position) {
						Intent mIntent = new Intent(activity, ShowApiActivity.class);
						mIntent.putExtra("showapi", listdata.get(position));
						activity.startActivity(mIntent);
					}
				});
			}
		});
		listview.setAdapter(mAdapter);
		if (listdata.size() != 0) {
			mAdapter.addData(listdata);
			showRefresh();
		} else {
			showRefresh();
		}
		listview.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
			@Override
			public void onRefresh() {
				showRefresh();
			}

			@Override
			public void onLoadMore() {
				if (allPages != 0 && pageNum == allPages) {// 判断是否到底了
					Toast.makeText(activity, R.string.already_nodata, Toast.LENGTH_SHORT).show();
				} else {
					pageNum++;
					requestData(1);
				}
			}
		});
	}

	public void showRefresh() {
		requestData(0);
	}

	public void requestData(final int what) {
		String url = "http://route.showapi.com/582-2";
		Map<String, String> body = new HashMap<String, String>();
		body.put("showapi_appid", "9106");
		body.put("showapi_timestamp", System.currentTimeMillis() + "");
		body.put("page", pageNum + "");
		body.put("typeId", id + "");
		body.put("showapi_sign", "7e391638939c4f7b9a2892851af098f9");
		HttpUtil.post(url, body, new ResponeCallback() {
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				try {
					JSONObject json = new JSONObject(result);
					if (json.getString("showapi_res_error").equals("")) {
						JSONObject pagebean = json.getJSONObject("showapi_res_body").getJSONObject("pagebean");
						JSONArray array = pagebean.getJSONArray("contentlist");
						allPages = pagebean.getInt("allPages");
						pageNum = pagebean.getInt("currentPage");
						ArrayList<ShowApi> bmobArr = new ArrayList<ShowApi>();
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							String contentImg = obj.getString("contentImg");
							String date = obj.getString("date");
							String id = obj.getString("id");
							String title = obj.getString("title");
							String typeId = obj.getString("typeId");
							String typeName = obj.getString("typeName");
							String url = obj.getString("url");
							String userLogo = obj.getString("userLogo");
							String userLogo_code = obj.getString("userLogo_code");
							String userName = obj.getString("userName");
							ShowApi showApi = new ShowApi(id, title, typeId, typeName, url, userLogo, userLogo_code,
									userName, date, contentImg);
							bmobArr.add(showApi);
						}
						if (pageNum==1){
							mAdapter.clearData();
						}
						mAdapter.addData(bmobArr);
						mAdapter.notifyDataSetChanged();
						listview.setPullLoadMoreCompleted();
						saveData(bmobArr);
					} else {
						String errMsg=json.getString("showapi_res_error");
						Toast.makeText(activity, errMsg, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String errmsg) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "onFailure", Toast.LENGTH_SHORT).show();
			}
		});
	}
	public void saveData(final ArrayList<ShowApi> bmobArr){
		new Thread(new Runnable() {
			@Override
			public void run() {
				dao.updateAll(bmobArr);
				BmobUtil.updateShowApiDataToBmob(activity, bmobArr);
			}
		}).start();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		ShowApi showapi = listdata.get(position);
		Intent mIntent = new Intent(activity, ShowApiActivity.class);
		mIntent.putExtra("showapi", showapi);
		startActivity(mIntent);

	}

}
