package com.hereafter.wxarticle.adapter;

import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.model.MenuItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeftMenuAdapter extends ArrayAdapter<MenuItem> {

	private LayoutInflater mInflater;

	private int mSelected;
	private int[] iconRes = new int[] { R.drawable.left_menu_jinxuan, R.drawable.left_menu_rank,
			R.drawable.left_menu_class, R.drawable.left_menu_fav, R.drawable.left_menu_feedback,
			R.drawable.left_menu_share };

	public LeftMenuAdapter(Context context, MenuItem[] objects) {
		super(context, -1, objects);

		mInflater = LayoutInflater.from(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_left_menu, parent, false);
		}

		ImageView iv = (ImageView) convertView.findViewById(R.id.id_item_icon);
		iv.setImageResource(iconRes[position]);
		TextView title = (TextView) convertView.findViewById(R.id.id_item_title);
		title.setText(getItem(position).getText());
		convertView.setBackgroundColor(Color.TRANSPARENT);
		title.setTextColor(Color.DKGRAY);
		if (position == mSelected) {
			convertView.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
			title.setTextColor(Color.WHITE);
		}

		return convertView;
	}

	public void setSelected(int position) {
		this.mSelected = position;
		notifyDataSetChanged();
	}

}
