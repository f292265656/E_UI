package com.plugin.control;

/*
 * Imageloader初始化
 */
import java.io.File;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.plugin.master.R;
import com.plugin.model.GlobalSetting;

public class ImageLoadHelper {
	private static DisplayImageOptions displayConf;
	private static ImageLoaderConfiguration config;
	private static File cacheDir;
	private static Drawable userUnknow;

	private static void instance() {
		if (displayConf != null && config != null) {
			return;
		}
		cacheDir = StorageUtils.getOwnCacheDirectory(
				GlobalSetting.getMainContext(), "E_UI/Plugin/SinaWeibo/Cache");
		userUnknow=GlobalSetting.getPluginContext().getResources().getDrawable(R.drawable.user_unknow);
		
		initLoderConfig();
		initDisplyConfig();
		ImageLoader.getInstance().init(config);
	}

	public static void load(String url, ImageView view) {
		instance();
		ImageLoader.getInstance().displayImage(url, view, displayConf);
	}

	private static void initLoderConfig() {

		config = new ImageLoaderConfiguration.Builder(
				GlobalSetting.getMainContext())
				.memoryCacheExtraOptions(150, 150)
				// max width, max height
				.denyCacheImageMultipleSizesInMemory()
				.diskCache(new UnlimitedDiscCache(cacheDir))
				// 保存路径
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(100)
				.writeDebugLogs().build();// 开始构建
	}

	private static void initDisplyConfig() {
		displayConf = new DisplayImageOptions.Builder()
				.showImageOnLoading(userUnknow)
				// 下载时显示图片
				.showImageOnFail(userUnknow)
				// 解码或下载错误显示
				.showImageForEmptyUri(userUnknow)
				// Url错误显示
				.cacheInMemory(true).cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(20)) // 圆角度数
				.displayer(new FadeInBitmapDisplayer(100)) // 渐入动画事件
				.build();
	}
}
