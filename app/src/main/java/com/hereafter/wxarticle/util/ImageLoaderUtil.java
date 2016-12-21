package com.hereafter.wxarticle.util;

import com.hereafter.wxarticle.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.widget.ImageView;

/**
 * 加载图片的工具类
 * 
 * @author 黄家强
 */
public class ImageLoaderUtil {
	public static DisplayImageOptions options_image, options_progress, options_grid;
	private static ImageLoader imageLoader;

	private static void initLoader(Context context) {
		options_image = new DisplayImageOptions.Builder().showStubImage(R.drawable.defaultpic)
				.showImageForEmptyUri(R.drawable.image_for_empty_url).cacheInMemory().cacheOnDisc()
				.imageScaleType(ImageScaleType.POWER_OF_2).displayer(new RoundedBitmapDisplayer(0xff424242, 10))
				.build();
		options_progress = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.image_for_empty_url)
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACT).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options_grid = new DisplayImageOptions.Builder().showStubImage(R.drawable.defaultpic)
				.showImageForEmptyUri(R.drawable.image_for_empty_url).cacheInMemory().cacheOnDisc().build();
	}

	public static ImageLoader getImageLoader(Context context) {
		if (imageLoader == null) {
			initLoader(context);
		}
		return imageLoader;
	}

	public static void displayImage(String url, ImageView imageView, Context context) {
		if (PreferenceUtil.getInstance(context).isLoadImg()) {
			getImageLoader(context).displayImage(url, imageView, options_image);
		} else {
			imageView.setImageResource(R.drawable.defaultpic);
		}
	}

	public static void displayImage(String url, ImageView imageView, Context context, DisplayImageOptions options) {
		getImageLoader(context).displayImage(url, imageView, options);
	}

	public static void displayImage(String url, ImageView imageView, Context context, DisplayImageOptions options,
			ImageLoadingListener imageLoadingListener) {
		getImageLoader(context).displayImage(url, imageView, options, imageLoadingListener);
	}

	public static void stopload(Context context) {
		getImageLoader(context).stop();
	}
}
