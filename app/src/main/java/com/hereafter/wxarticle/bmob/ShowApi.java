package com.hereafter.wxarticle.bmob;

import java.util.List;

import com.hereafter.wxarticle.util.LogUtil;

import android.content.Context;
import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ShowApi extends BmobObject {
	private String id;
	private String title;
	private String typeId;
	private String typeName;
	private String url;
	private String userLogo;
	private String userLogo_code;
	private String userName;
	private String date;
	private String contentImg;
	private BmobRelation collect;
	private BmobRelation like;
	private BmobRelation comment;

	public ShowApi() {
		super("ShowApi");
		// TODO Auto-generated constructor stub
	}

	public ShowApi(String id, String title, String typeId, String typeName, String url, String userLogo,
			String userLogo_code, String userName, String date, String contentImg) {
		super();
		this.id = id;
		this.title = title;
		this.typeId = typeId;
		this.typeName = typeName;
		this.url = url;
		this.userLogo = userLogo;
		this.userLogo_code = userLogo_code;
		this.userName = userName;
		this.date = date;
		this.contentImg = contentImg;
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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserLogo() {
		return userLogo;
	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public String getUserLogo_code() {
		return userLogo_code;
	}

	public void setUserLogo_code(String userLogo_code) {
		this.userLogo_code = userLogo_code;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContentImg() {
		return contentImg;
	}

	public void setContentImg(String contentImg) {
		this.contentImg = contentImg;
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
		return "ShowApi [id=" + id + ", title=" + title + ", typeId=" + typeId + ", typeName=" + typeName + ", url="
				+ url + ", userLogo=" + userLogo + ", userLogo_code=" + userLogo_code + ", userName=" + userName
				+ ", date=" + date + ", contentImg=" + contentImg + "]";
	}

	public void insertIfNoExist(final Context context) {
		BmobQuery<ShowApi> query = new BmobQuery<ShowApi>();
		query.addWhereEqualTo("id", id);
		query.findObjects(context, new FindListener<ShowApi>() {

			@Override
			public void onSuccess(List<ShowApi> arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null || arg0.size() == 0) {
					save(context);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtil.d("onError:" + arg0 + "," + arg1);
			}
		});
	}

	/**
	 * 用户是否为该数据点过赞
	 * 
	 * @param user
	 * @return
	 */
	public void getLike(Context context, FindListener<MyUser> findListener) {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		ShowApi mShow = new ShowApi();
		mShow.setObjectId(getObjectId());
		query.addWhereRelatedTo("like", new BmobPointer(mShow));
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
				ShowApi mShow = new ShowApi();
				mShow.setObjectId(getObjectId());
				if (isAdd) {
					relation.remove(user);
				} else {
					relation.add(user);
				}
				mShow.setLike(relation);
				mShow.update(context, listener);
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
		ShowApi mShow = new ShowApi();
		mShow.setObjectId(getObjectId());
		query.addWhereRelatedTo("collect", new BmobPointer(mShow));
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
				ShowApi mShow = new ShowApi();
				mShow.setObjectId(getObjectId());
				if (isAdd) {
					relation.remove(user);
				} else {
					relation.add(user);
				}
				mShow.setCollect(relation);
				mShow.update(context, listener);
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
		ShowApi mShow = new ShowApi();
		mShow.setObjectId(getObjectId());
		query.addWhereRelatedTo("comment", new BmobPointer(mShow));
		query.include("user");
		query.findObjects(context, findListener);
	}

}
