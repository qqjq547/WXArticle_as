package com.hereafter.wxarticle.bmob;

import java.util.LinkedList;
import java.util.List;

import com.hereafter.wxarticle.util.LogUtil;

import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyUser extends BmobUser {
	private static final long serialVersionUID = 1L;
	private int sex;
	private String birth_year;
	private String headimg;
	private String nickname;
	private String city;
	private BmobRelation collect;
	private BmobRelation collect_showapi;
	private BmobRelation comment;
	private List<String> like = new LinkedList<String>();

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getBirth_year() {
		return birth_year;
	}

	public void setBirth_year(String birth_year) {
		this.birth_year = birth_year;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public BmobRelation getCollect() {
		return collect;
	}

	public void setCollect(BmobRelation collect) {
		this.collect = collect;
	}

	public BmobRelation getCollect_showapi() {
		return collect_showapi;
	}

	public void setCollect_showapi(BmobRelation collect_showapi) {
		this.collect_showapi = collect_showapi;
	}

	public BmobRelation getComment() {
		return comment;
	}

	public void setComment(BmobRelation comment) {
		this.comment = comment;
	}

	public List<String> getLike() {
		return like;
	}

	public void setLike(List<String> like) {
		this.like = like;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void getCollectJuheByUser(Context context, FindListener<Juhe> findListener) {
		BmobQuery<Juhe> query = new BmobQuery<Juhe>();
		MyUser mUser = new MyUser();
		mUser.setObjectId(getObjectId());
		query.addWhereRelatedTo("collect", new BmobPointer(mUser));
		query.findObjects(context, findListener);
	}

	public void getCollectShowApiByUser(Context context, FindListener<ShowApi> findListener) {
		BmobQuery<ShowApi> query = new BmobQuery<ShowApi>();
		MyUser mUser = new MyUser();
		mUser.setObjectId(getObjectId());
		query.addWhereRelatedTo("collect_showapi", new BmobPointer(mUser));
		query.findObjects(context, findListener);
	}

	public void addJuheCollect(final Context context, final Juhe juhe, final UpdateListener listener) {
		getCollectJuheByUser(context, new FindListener<Juhe>() {

			@Override
			public void onSuccess(List<Juhe> juhes) {
				// TODO Auto-generated method stub
				boolean isAdd = false;
				for (int i = 0; i < juhes.size(); i++) {
					if (juhes.get(i).getObjectId().equals(juhe.getObjectId())) {
						isAdd = true;
						break;
					}
				}
				BmobRelation relation = new BmobRelation();
				MyUser mUser1 = new MyUser();
				mUser1.setObjectId(getObjectId());
				if (isAdd) {
					LogUtil.d("remove");
					relation.remove(juhe);
				} else {
					LogUtil.d("add");
					relation.add(juhe);
				}
				mUser1.setCollect(relation);
				mUser1.update(context, listener);

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void addShowApiCollect(final Context context, final ShowApi showapi, final UpdateListener listener) {
		getCollectShowApiByUser(context, new FindListener<ShowApi>() {

			@Override
			public void onSuccess(List<ShowApi> shows) {
				// TODO Auto-generated method stub
				boolean isAdd = false;
				for (int i = 0; i < shows.size(); i++) {
					if (shows.get(i).getObjectId().equals(showapi.getObjectId())) {
						isAdd = true;
						break;
					}
				}
				BmobRelation relation = new BmobRelation();
				MyUser mUser1 = new MyUser();
				mUser1.setObjectId(getObjectId());
				if (isAdd) {
					LogUtil.d("remove");
					relation.remove(showapi);
				} else {
					LogUtil.d("add");
					relation.add(showapi);
				}
				mUser1.setCollect_showapi(relation);
				mUser1.update(context, listener);

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public boolean islikeJuhe(Juhe juhe) {
		if (like.contains(juhe.getId())) {
			return true;
		} else {
			like.add(juhe.getId());
			return false;
		}
	}

	public boolean islikeShowApi(ShowApi show) {
		if (like.contains(show.getId())) {
			return true;
		} else {
			like.add(show.getId());
			return false;
		}
	}
}
