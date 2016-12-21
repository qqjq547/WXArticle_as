package com.hereafter.wxarticle.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.ShowApiActivity;
import com.hereafter.wxarticle.bmob.ShowApi;
import com.hereafter.wxarticle.interf.RecyItemClickListener;
import com.hereafter.wxarticle.interf.RecyItemLongClickListener;
import com.hereafter.wxarticle.util.ImageLoaderUtil;
import com.hereafter.wxarticle.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyAdapter extends RecyclerView.Adapter<ViewHolder> {
	private Context context;
	private ArrayList<ShowApi> items = new ArrayList<ShowApi>();
	private RecyItemClickListener recyItemClickListener;
	private RecyItemLongClickListener recyItemlongClickListener;

	public void setRecyItemClickListener(RecyItemClickListener recyItemClickListener) {
		this.recyItemClickListener = recyItemClickListener;
	}

	public void setRecyItemlongClickListener(RecyItemLongClickListener recyItemlongClickListener) {
		this.recyItemlongClickListener = recyItemlongClickListener;
	}

	public RecyAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewholder, int position) {
		// TODO Auto-generated method stub
		if (viewholder instanceof Holder) {
			final ShowApi showapi = items.get(position);
			Holder holder = (Holder) viewholder;
			ImageLoaderUtil.displayImage(showapi.getUserLogo(), holder.logo, context);
			holder.username.setText(showapi.getUserName());
			holder.title.setText(showapi.getTitle());
			holder.date.setText(Utils.getDateStr(showapi.getDate()));
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(context).inflate(R.layout.item_recy, group, false);
		return new Holder(v,recyItemClickListener,recyItemlongClickListener);
	}

	public void addData(ArrayList<ShowApi> data) {
		int size = items.size();
		this.items.addAll(data);
		this.notifyItemRangeInserted(size, data.size() - 1);
	}

	public void clearData() {
		int size = this.items.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				items.remove(0);
			}
			this.notifyItemRangeRemoved(0, size - 1);
		}

	}

	public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
		private ImageView logo;
		private TextView username;
		private TextView title;
		private TextView date;
		private RecyItemClickListener itemClickListener;
		private RecyItemLongClickListener itemLongClickListener;
		public Holder(View itemView, RecyItemClickListener itemClickListener, RecyItemLongClickListener itemLongClickListener) {
			super(itemView);
			// TODO Auto-generated constructor stub
			logo = (ImageView) itemView.findViewById(R.id.userlogo);
			username = (TextView) itemView.findViewById(R.id.username);
			title = (TextView) itemView.findViewById(R.id.title);
			date = (TextView) itemView.findViewById(R.id.date);
			this.itemClickListener = itemClickListener;
			this.itemLongClickListener = itemLongClickListener;
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}
		@Override
		public void onClick(View view) {
			if (itemClickListener!=null)
				itemClickListener.onItemClick(this,getAdapterPosition());
		}

		@Override
		public boolean onLongClick(View view) {
			if (itemLongClickListener!=null)
				itemLongClickListener.onItemLongClick(this,getAdapterPosition());
			return true;
		}
	}


}
