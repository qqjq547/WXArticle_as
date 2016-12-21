package com.hereafter.wxarticle.widget;

import com.hereafter.wxarticle.R;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PullLoadMoreRecyclerView extends LinearLayout {
	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private PullLoadMoreListener mPullLoadMoreListener;
	private boolean hasMore = true;
	private boolean isRefresh = false;
	private boolean isLoadMore = false;
	private boolean pullRefreshEnable = true;
	private boolean pushRefreshEnable = true;
	private View mFooterView;
	private Context mContext;
	private TextView loadMoreText;

	public PullLoadMoreRecyclerView(Context context) {
		super(context);
		initView(context);
	}

	public PullLoadMoreRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		mContext = context;
		View view = LayoutInflater.from(context).inflate(R.layout.pull_loadmore_layout, null);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
				R.color.swiperefresh_color3, R.color.swiperefresh_color4);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh(this));
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mRecyclerView.setVerticalScrollBarEnabled(true);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setOnScrollListener(new RecyclerViewOnScroll(this));
		mRecyclerView.setOnTouchListener(new onTouchRecyclerView());
		mFooterView = view.findViewById(R.id.footerView);
		loadMoreText = (TextView) view.findViewById(R.id.text);
		mFooterView.setVisibility(View.GONE);
		this.addView(view);

	}

	/**
	 * LinearLayoutManager
	 */
	public void setLinearLayout() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
	}

	/**
	 * GridLayoutManager
	 */

	public void setGridLayout(int spanCount) {

		GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
		gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(gridLayoutManager);
	}

	/**
	 * StaggeredGridLayoutManager
	 */

	public void setStaggeredGridLayout(int spanCount) {
		StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount,
				LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
	}

	public RecyclerView.LayoutManager getLayoutManager() {
		return mRecyclerView.getLayoutManager();
	}

	public RecyclerView getRecyclerView() {
		return mRecyclerView;
	}

	public void scrollToTop() {
		mRecyclerView.scrollToPosition(0);
	}

	public void setAdapter(RecyclerView.Adapter adapter) {
		if (adapter != null) {
			mRecyclerView.setAdapter(adapter);
		}
	}

	public void setPullRefreshEnable(boolean enable) {
		pullRefreshEnable = enable;
		setSwipeRefreshEnable(enable);
	}

	public boolean getPullRefreshEnable() {
		return pullRefreshEnable;
	}

	public void setSwipeRefreshEnable(boolean enable) {
		mSwipeRefreshLayout.setEnabled(enable);
	}

	public boolean getSwipeRefreshEnable() {
		return mSwipeRefreshLayout.isEnabled();
	}

	public void setColorSchemeResources(int... colorResIds) {
		mSwipeRefreshLayout.setColorSchemeResources(colorResIds);

	}

	public SwipeRefreshLayout getSwipeRefreshLayout() {
		return mSwipeRefreshLayout;
	}

	public void setRefreshing(final boolean isRefreshing) {
		mSwipeRefreshLayout.post(new Runnable() {

			@Override
			public void run() {
				if (pullRefreshEnable)
					mSwipeRefreshLayout.setRefreshing(isRefreshing);
			}
		});

	}

	/**
	 * Solve IndexOutOfBoundsException exception
	 */
	public class onTouchRecyclerView implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (isRefresh || isLoadMore) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getPushRefreshEnable() {
		return pushRefreshEnable;
	}

	public void setPushRefreshEnable(boolean pushRefreshEnable) {
		this.pushRefreshEnable = pushRefreshEnable;
	}

	public void setFooterViewText(CharSequence text) {
		loadMoreText.setText(text);
	}

	public void setFooterViewText(int resid) {
		loadMoreText.setText(resid);
	}

	public void refresh() {
		if (mPullLoadMoreListener != null) {
			mPullLoadMoreListener.onRefresh();
		}
	}

	public void loadMore() {
		if (mPullLoadMoreListener != null && hasMore) {
			mFooterView.setVisibility(View.VISIBLE);
			invalidate();
			mPullLoadMoreListener.onLoadMore();

		}
	}

	public void setPullLoadMoreCompleted() {
		isRefresh = false;
		mSwipeRefreshLayout.setRefreshing(false);

		isLoadMore = false;
		mFooterView.setVisibility(View.GONE);

	}

	public void setOnPullLoadMoreListener(PullLoadMoreListener listener) {
		mPullLoadMoreListener = listener;
	}

	public boolean isLoadMore() {
		return isLoadMore;
	}

	public void setIsLoadMore(boolean isLoadMore) {
		this.isLoadMore = isLoadMore;
	}

	public boolean isRefresh() {
		return isRefresh;
	}

	public void setIsRefresh(boolean isRefresh) {
		this.isRefresh = isRefresh;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public interface PullLoadMoreListener {
		void onRefresh();

		void onLoadMore();
	}

	public class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener {
		private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

		public SwipeRefreshLayoutOnRefresh(PullLoadMoreRecyclerView pullLoadMoreRecyclerView) {
			this.mPullLoadMoreRecyclerView = pullLoadMoreRecyclerView;
		}

		@Override
		public void onRefresh() {
			if (!mPullLoadMoreRecyclerView.isRefresh()) {
				mPullLoadMoreRecyclerView.setIsRefresh(true);
				mPullLoadMoreRecyclerView.refresh();
			}
		}
	}

	public class RecyclerViewOnScroll extends RecyclerView.OnScrollListener {
		private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

		public RecyclerViewOnScroll(PullLoadMoreRecyclerView pullLoadMoreRecyclerView) {
			this.mPullLoadMoreRecyclerView = pullLoadMoreRecyclerView;
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			int lastCompletelyVisibleItem = 0;
			int firstVisibleItem = 0;
			RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
			int totalItemCount = layoutManager.getItemCount();
			if (layoutManager instanceof GridLayoutManager) {
				GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
				// Position to find the final item of the current LayoutManager
				lastCompletelyVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
				firstVisibleItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
			} else if (layoutManager instanceof LinearLayoutManager) {
				LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
				lastCompletelyVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
				firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
			} else if (layoutManager instanceof StaggeredGridLayoutManager) {
				StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
				// since may lead to the final item has more than one
				// StaggeredGridLayoutManager the particularity of the so here
				// that is an array
				// this array into an array of position and then take the
				// maximum value that is the last show the position value
				int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
				staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
				lastCompletelyVisibleItem = findMax(lastPositions);
				firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions)[0];
			}
			if (firstVisibleItem == 0) {
				if (mPullLoadMoreRecyclerView.getPullRefreshEnable())
					mPullLoadMoreRecyclerView.setSwipeRefreshEnable(true);
			} else {
				mPullLoadMoreRecyclerView.setSwipeRefreshEnable(false);
			}
			if (mPullLoadMoreRecyclerView.getPushRefreshEnable() && !mPullLoadMoreRecyclerView.isRefresh()
					&& mPullLoadMoreRecyclerView.isHasMore() && (lastCompletelyVisibleItem == totalItemCount - 1)
					&& !mPullLoadMoreRecyclerView.isLoadMore() && (dx > 0 || dy > 0)) {
				mPullLoadMoreRecyclerView.setIsLoadMore(true);
				mPullLoadMoreRecyclerView.loadMore();
			}

		}
		// To find the maximum value in the array

		private int findMax(int[] lastPositions) {

			int max = lastPositions[0];
			for (int value : lastPositions) {
				// int max = Math.max(lastPositions,value);
				if (value > max) {
					max = value;
				}
			}
			return max;
		}
	}
}
