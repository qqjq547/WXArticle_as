package com.hereafter.wxarticle.bmob;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class Juhe extends BmobObject {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String source;
	private String firstImg;
	private String mark;
	private String url;
	private BmobRelation collect;
	private BmobRelation like;
	private BmobRelation comment;

	public Juhe() {
		super("Juhe");
		// TODO Auto-generated constructor stub
	}

	public Juhe(String id, String title, String source, String firstImg, String mark, String url) {
		super();
		this.id = id;
		this.title = title;
		this.source = source;
		this.firstImg = firstImg;
		this.mark = mark;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getFirstImg() {
		return firstImg;
	}

	public void setFirstImg(String firstImg) {
		this.firstImg = firstImg;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BmobRelation getCollect() {
		return collect;
	}

	public void setCollect(BmobRelation collect) {
		this.collect = collect;
	}

	public BmobRelation getLike() {
		return like;
	}

	public void setLike(BmobRelation like) {
		this.like = like;
	}

	public BmobRelation getComment() {
		return comment;
	}

	public void setComment(BmobRelation comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Juhe [id=" + id + ", title=" + title + ", source=" + source + ", firstImg=" + firstImg + ", mark="
				+ mark + ", url=" + url + "]";
	}

	/**
	 * 如果数据库没有该数据则插入
	 * 
	 * @param context
	 */
	public void insertIfNoExist(final Context context) {
		// BmobQuery<Juhe> query = new BmobQuery<Juhe>();
		// query.addWhereEqualTo("id", id);
		// query.findObjects(context, new FindListener<Juhe>() {
		// @Override
		// public void onSuccess(List<Juhe> arg0) {
		// // TODO Auto-generated method stub
		// LogUtil.d("onSuccess:arg0.size()=" + arg0.size());
		// if (arg0==null||arg0.size()==0) {
		// save(context);
		// }
		// }
		//
		// @Override
		// public void onError(int arg0, String arg1) {
		// // TODO Auto-generated method stub
		// LogUtil.d("onError:" + arg0 + "," + arg1);
		//
		// }
		//
		// });
		save(context);
	}

	/**
	 * 用户是否为该数据点过赞
	 * 
	 * @param user
	 * @return
	 */
	public void getLike(Context context, FindListener<MyUser> findListener) {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		Juhe mJuhe = new Juhe();
		mJuhe.setObjectId(getObjectId());
		query.addWhereRelatedTo("like", new BmobPointer(mJuhe));
		query.findObjects(context, findListener);
	}

	public void addLikeData(final Context context, final MyUser user, final UpdateListener listener) {
		getLike(context, new FindListener<MyUser>() {

			@Override
			public void onSuccess(List<MyUser> list) {
				// TODO Auto-generated method stub
				boolean isAdd = false;
				for (int i = 0; i < list.size(); i++) {
					if (user.getObjectId().equals(list.get(i).getObjectId())) {
						isAdd = true;
						break;
					}
				}
				BmobRelation relation = new BmobRelation();
				Juhe juhe1 = new Juhe();
				juhe1.setObjectId(getObjectId());
				if (isAdd) {
					relation.remove(user);
				} else {
					relation.add(user);
				}
				juhe1.setLike(relation);
				juhe1.update(context, listener);
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 获取收藏该数据的用户
	 * 
	 * @param context
	 * @param findListener
	 */
	public void getCollectByData(Context context, FindListener<MyUser> findListener) {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		Juhe mJuhe = new Juhe();
		mJuhe.setObjectId(getObjectId());
		query.addWhereRelatedTo("collect", new BmobPointer(mJuhe));
		query.findObjects(context, findListener);
	}

	public static void getCollectByData1(Context context, FindListener<Juhe> findListener) {
		BmobQuery<Juhe> query = new BmobQuery<Juhe>();
		MyUser user = MyUser.getCurrentUser(context, MyUser.class);
		BmobPointer point = new BmobPointer(user);
		BmobPointer[] users = new BmobPointer[] { point };
		query.addWhereContainedIn("collect", Arrays.asList(users));
		query.findObjects(context, findListener);
	}

	public void addCollectData(final Context context, final MyUser user, final UpdateListener listener) {
		getCollectByData(context, new FindListener<MyUser>() {

			@Override
			public void onSuccess(List<MyUser> list) {
				// TODO Auto-generated method stub
				boolean isAdd = false;
				for (int i = 0; i < list.size(); i++) {
					if (user.getObjectId().equals(list.get(i).getObjectId())) {
						isAdd = true;
						break;
					}
				}
				BmobRelation relation = new BmobRelation();
				Juhe juhe1 = new Juhe();
				juhe1.setObjectId(getObjectId());
				if (isAdd) {
					relation.remove(user);
				} else {
					relation.add(user);
				}
				juhe1.setCollect(relation);
				juhe1.update(context, listener);
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public BmobACL getACL() {
		// TODO Auto-generated method stub
		return super.getACL();
	}

	/**
	 * 获取该数据的评论
	 * 
	 * @param context
	 * @param findListener
	 */
	public void getComments(Context context, FindListener<Comment> findListener) {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		Juhe mJuhe = new Juhe();
		mJuhe.setObjectId(getObjectId());
		query.addWhereRelatedTo("comment", new BmobPointer(mJuhe));
		query.include("user");
		query.findObjects(context, findListener);
	}

}
