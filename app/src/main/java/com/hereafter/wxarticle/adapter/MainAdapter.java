package com.hereafter.wxarticle.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.hereafter.wxarticle.R;
import com.hereafter.wxarticle.bmob.Juhe;
import com.hereafter.wxarticle.bmob.ShowApi;
import com.hereafter.wxarticle.interf.RecyItemClickListener;
import com.hereafter.wxarticle.interf.RecyItemLongClickListener;
import com.hereafter.wxarticle.util.ImageLoaderUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
	private Context context;
	private ArrayList<Juhe> items = new ArrayList<Juhe>();
	private RecyItemClickListener recyItemClickListener;
	private RecyItemLongClickListener recyItemlongClickListener;

	public MainAdapter(Context context, ArrayList<Juhe> items) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.items = items;
	}

	public void setRecyItemClickListener(RecyItemClickListener recyItemClickListener) {
		this.recyItemClickListener = recyItemClickListener;
	}

	public void setRecyItemlongClickListener(RecyItemLongClickListener recyItemlongClickListener) {
		this.recyItemlongClickListener = recyItemlongClickListener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
		return new Holder(v,recyItemClickListener,recyItemlongClickListener);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
		if (viewholder instanceof Holder) {
			final Juhe juhe = items.get(position);
			Holder holder = (Holder) viewholder;
			holder.title.setText(juhe.getTitle());
			holder.time.setText(parseDate(juhe.getId()));
			ImageLoaderUtil.displayImage(juhe.getFirstImg(), holder.img, context);
			holder.source.setText(juhe.getSource());
		}
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
	public void addData(ArrayList<Juhe> data) {
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

	class Holder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
		private TextView title;
		private TextView time;
		private ImageView img;
		private TextView source;
        private RecyItemClickListener itemClickListener;
		private RecyItemLongClickListener itemLongClickListener;
		public Holder(View itemView, RecyItemClickListener itemClickListener, RecyItemLongClickListener itemLongClickListener) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
			time = (TextView) itemView.findViewById(R.id.time);
			img = (ImageView) itemView.findViewById(R.id.img);
			source = (TextView) itemView.findViewById(R.id.source);
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

	private String parseDate(String juhedate) {
		String timestr = juhedate.substring(7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf_today = new SimpleDateFormat("hh:mm");
		SimpleDateFormat sdf_before = new SimpleDateFormat("yy-MM-dd");
		try {
			Date date = sdf.parse(timestr);
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
