package com.hereafter.wxarticle.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.ShowApiActivity;
import com.hereafter.wxarticle.adapter.RecyAdapter;
import com.hereafter.wxarticle.bmob.ShowApi;
import com.hereafter.wxarticle.dao.ShowApiDao;
import com.hereafter.wxarticle.http.HttpUtil;
import com.hereafter.wxarticle.http.ResponeCallback;
import com.hereafter.wxarticle.interf.RecyItemClickListener;
import com.hereafter.wxarticle.util.BmobUtil;
import com.hereafter.wxarticle.util.LogUtil;
import com.hereafter.wxarticle.widget.PullLoadMoreRecyclerView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ShowRecyFragment extends Fragment  {
	private View root;
	private PullLoadMoreRecyclerView listview;
	private Activity activity;
	private final OkHttpClient client = new OkHttpClient();
	private int pageNum = 1;
	private int allPages = 0;
	private ArrayList<ShowApi> listdata = new ArrayList<ShowApi>();
	private RecyAdapter mAdapter;
	private ShowApiDao showapiDao;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
		showapiDao = ShowApiDao.getInstance(activity);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.fragment_recy1, null);
		listview = (PullLoadMoreRecyclerView) root.findViewById(R.id.pullLoadMoreRecyclerView);
		listview.setLinearLayout();
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		listdata = showapiDao.queryAll();
		mAdapter = new RecyAdapter(activity);
		mAdapter.setRecyItemClickListener(new RecyItemClickListener() {
			@Override
			public void onItemClick(RecyclerView.ViewHolder holder, int position) {
				Intent mIntent = new Intent(activity, ShowApiActivity.class);
				mIntent.putExtra("showapi", listdata.get(position));
				activity.startActivity(mIntent);
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
		body.put("showapi_timestamp", String.valueOf(System.currentTimeMillis()));
		body.put("page", String.valueOf(pageNum));
		body.put("showapi_sign", "7e391638939c4f7b9a2892851af098f9");
		HttpUtil.post(url, body, new ResponeCallback() {
			@Override
			public void onSuccess(String result) {
				LogUtil.d("requestData="+result);
				super.onSuccess(result);
				try {
					JSONObject json = new JSONObject(result);
					if (json.get("showapi_res_error").equals("")) {
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
							ShowApi showApi = new ShowApi(id, title, typeId, typeName, url, userLogo,
									userLogo_code, userName, date, contentImg);
							bmobArr.add(showApi);
						}
						if (pageNum==1){
							mAdapter.clearData();
							listdata.clear();
						}
						listdata.addAll(bmobArr);
						mAdapter.addData(bmobArr);
						listview.setPullLoadMoreCompleted();
					}else{
						Toast.makeText(activity,"获取失败",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}
	public void setData(final ArrayList<ShowApi> bmobArr){
		new Thread(new Runnable() {
			@Override
			public void run() {
				showapiDao.updateAll(listdata);
				BmobUtil.updateShowApiDataToBmob(activity, bmobArr);
			}
		}).start();

	}

}
