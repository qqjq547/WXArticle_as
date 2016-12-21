package com.hereafter.wxarticle.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Comment extends BmobObject {
	private String articleid;
	private String content;
	private BmobDate time;
	private MyUser user;

	public Comment(String articleid, String content, BmobDate time, MyUser user) {
		super();
		this.articleid = articleid;
		this.content = content;
		this.time = time;
		this.user = user;
	}

	public String getArticleid() {
		return articleid;
	}

	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BmobDate getTime() {
		return time;
	}

	public void setTime(BmobDate time) {
		this.time = time;
	}

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Comment [articleid=" + articleid + ", content=" + content + ", time=" + time + ", user=" + user + "]";
	}
}
