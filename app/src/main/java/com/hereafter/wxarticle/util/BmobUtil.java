package com.hereafter.wxarticle.util;

import java.util.ArrayList;
import java.util.List;

import com.hereafter.wxarticle.bmob.Juhe;
import com.hereafter.wxarticle.bmob.MyUser;
import com.hereafter.wxarticle.bmob.ShowApi;

import android.content.Context;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BmobUtil {
	/**
	 * 更新聚合数据到bmob
	 * 
	 * @param context
	 * @param array聚合数据
	 */
	public static void updateJuheDataToBmob(Context context, ArrayList<Juhe> array) {
		List<BmobObject> persons = new ArrayList<BmobObject>();
		for (int i = array.size() - 1; i >= 0; i--) {
			Juhe juhe = array.get(i);
			persons.add(juhe);
		}
		new BmobObject().insertBatch(context, persons, new SaveListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				LogUtil.i("批量添加juhe成功");
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				LogUtil.i("批量添加juhe失败");
			}
		});
	}

	/**
	 * 更新ShowApi数据到bmob
	 * 
	 * @param context
	 * @param array
	 *            ShowApi数据
	 */
	public static void updateShowApiDataToBmob(Context context, ArrayList<ShowApi> array) {
		List<BmobObject> objList = new ArrayList<BmobObject>();
		for (int i = array.size() - 1; i >= 0; i--) {
			ShowApi show = array.get(i);
			objList.add(show);
		}
		new BmobObject().insertBatch(context, objList, new SaveListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				LogUtil.i("批量添加show成功");
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				LogUtil.i("批量添加show失败");
			}
		});
	}

	/**
	 * 更新juhe的收藏，在对应的聚合数据上更新收藏信息
	 * 
	 * @param context
	 * @param juhe
	 *            聚合数据
	 * @param listener
	 *            更新监听
	 */
	public static void collecJuheByUser(final Context context, final Juhe juhe, final UpdateListener listener) {
		final MyUser user = MyUser.getCurrentUser(context, MyUser.class);
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		MyUser mUser = new MyUser();
		mUser.setObjectId(user.getObjectId());
		query.addWhereRelatedTo("collect", new BmobPointer(mUser));
		query.findObjects(context, new FindListener<MyUser>() {

			@Override
			public void onSuccess(List<MyUser> object) {
				// TODO Auto-generated method stub
				Juhe mJuhe = new Juhe();
				mJuhe.setObjectId(juhe.getObjectId());
				BmobRelation relation = new BmobRelation();
				if (object != null && object.size() > 0) {
					relation.remove(user);
				} else {
					relation.add(user);
				}
				mJuhe.setCollect(relation);
				mJuhe.update(context, listener);

			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				LogUtil.i("查询失败：" + code + "-" + msg);
			}
		});

	}

	/**
	 * 更新showapi的收藏，在对应的showapi数据上更新收藏信息
	 * 
	 * @param context
	 * @param juhe
	 *            showapi数据
	 * @param listener
	 *            更新监听
	 */
	public static void collecShowApiByUser(final Context context, final ShowApi showapi,
			final UpdateListener listener) {
		final MyUser user = MyUser.getCurrentUser(context, MyUser.class);
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		MyUser mUser = new MyUser();
		mUser.setObjectId(user.getObjectId());
		query.addWhereRelatedTo("collect", new BmobPointer(mUser));
		query.findObjects(context, new FindListener<MyUser>() {

			@Override
			public void onSuccess(List<MyUser> object) {
				// TODO Auto-generated method stub
				ShowApi mshow = new ShowApi();
				mshow.setObjectId(showapi.getObjectId());
				BmobRelation relation = new BmobRelation();
				if (object != null && object.size() > 0) {
					relation.remove(user);
				} else {
					relation.add(user);
				}
				mshow.setCollect(relation);
				mshow.update(context, listener);

			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				LogUtil.i("查询失败：" + code + "-" + msg);
			}
		});
	}

	/**
	 * 更新用户的收藏，在用户表上更新收藏信息
	 * 
	 * @param context
	 * @param juhe
	 * @param listener
	 */
	public static void userCollectJuhe(final Context context, final Juhe juhe, final UpdateListener listener) {
		final MyUser user = MyUser.getCurrentUser(context, MyUser.class);
		BmobQuery<Juhe> query = new BmobQuery<Juhe>();
		MyUser mUser = new MyUser();
		mUser.setObjectId(user.getObjectId());
		query.addWhereRelatedTo("collect", new BmobPointer(mUser));
		query.findObjects(context, new FindListener<Juhe>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtil.d("onError=" + arg1);
			}

			@Override
			public void onSuccess(List<Juhe> arg0) {
				// TODO Auto-generated method stub
				LogUtil.d("size=" + arg0.size());
				Juhe target = null;
				for (int i = 0; i < arg0.size(); i++) {
					LogUtil.d("getObjectId=" + arg0.get(i).getObjectId());
					if (arg0.get(i).getObjectId().equals(juhe.getObjectId())) {
						target = arg0.get(i);
					}
				}
				BmobRelation relation = new BmobRelation();
				MyUser mUser1 = new MyUser();
				mUser1.setObjectId(user.getObjectId());
				if (target != null) {
					LogUtil.d("remove");
					relation.remove(juhe);
				} else {
					LogUtil.d("add");
					relation.add(juhe);
				}
				mUser1.setCollect(relation);
				mUser1.update(context, listener);
			}
		});

	}
}
