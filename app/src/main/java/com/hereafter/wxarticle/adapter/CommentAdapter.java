package com.hereafter.wxarticle.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.bmob.Comment;
import com.hereafter.wxarticle.util.ImageLoaderUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Comment> items = new ArrayList<Comment>();

	public CommentAdapter(Context context, ArrayList<Comment> items) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder mHolder;
		Comment comment = items.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
			mHolder = new Holder();
			mHolder.logo = (ImageView) convertView.findViewById(R.id.userlogo);
			mHolder.username = (TextView) convertView.findViewById(R.id.username);
			mHolder.content = (TextView) convertView.findViewById(R.id.content);
			mHolder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		ImageLoaderUtil.displayImage(comment.getUser().getHeadimg(), mHolder.logo, context);
		mHolder.username.setText(comment.getUser().getUsername());
		mHolder.content.setText(comment.getContent());
		// mHolder.date.setText(getDateStr(comment.getTime().));
		mHolder.date.setText(getDateStr(comment.getTime().getDate()));
		return convertView;
	}

	class Holder {
		private ImageView logo;
		private TextView username;
		private TextView content;
		private TextView date;
	}

	public String getDateStr(String formatdate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		SimpleDateFormat sdf_today = new SimpleDateFormat("hh:mm");
		SimpleDateFormat sdf_before = new SimpleDateFormat("yy-MM-dd");
		try {
			Date date = sdf.parse(formatdate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			Calendar curcal = Calendar.getInstance();
			int d = curcal.get(Calendar.DATE) - cal.get(Calendar.DATE);
			if (d == 0) {
				return sdf_today.format(date);
			} else if (d == 1) {
				return "昨天";
			} else if (d == 2) {
				return "前天";
			} else {
				return sdf_before.format(date);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
