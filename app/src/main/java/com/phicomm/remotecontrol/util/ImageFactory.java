package com.phicomm.remotecontrol.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.phicomm.remotecontrol.R;


/**
 * Created by hao04.wu on 2017/4/5.
 */

public class ImageFactory {
    public static final String CACHE_DIR = ".phicommm_home_cahce";

    //    public static int maxImageWidthForMemoryCache = 1000;
//    public static int maxImageHeightForMemoryCache = 1000;
//    public static ImageLoaderConfiguration getConfiguration(Context context) {
//        String cache_dir = context.getExternalFilesDir(CACHE_DIR).getAbsolutePath();
//        File cacheDir = new File(cache_dir);
//        if (!cacheDir.exists()) {
//            cacheDir.mkdirs();
//        }
//        int memoryCacheSize = (int) (Math.min(Runtime.getRuntime().maxMemory() / 8, 16 * 1024 * 1024));
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .memoryCacheExtraOptions(maxImageWidthForMemoryCache, maxImageHeightForMemoryCache)//图片使用最好采用720P制定View改取得的图片尺寸
//                .threadPoolSize(5)
//                .threadPriority(Thread.NORM_PRIORITY - 1)
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCacheSize(memoryCacheSize)
//                .diskCache(new UnlimitedDiskCache(cacheDir))
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .build();
//        return config;
//    }
//    public static void initImageLoader(Context context) {
//        ImageLoader.getInstance().init(getConfiguration(context));
//    }
    public static DisplayImageOptions getDefaultImageOptions() {
        int imageRes = R.mipmap.ic_launcher;
        return getDefaultImageOptions(imageRes);
    }

    public static DisplayImageOptions getDefaultImageOptions(int imageRes) {
        return getImageOptions(imageRes, imageRes, imageRes);
    }

    public static DisplayImageOptions getImageOptions(int stubImageRes, int imageRes, int failImageRes) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(stubImageRes)//有可能引起图片加载闪烁
                .showImageForEmptyUri(imageRes)
                .showImageOnFail(failImageRes)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new FadeInBitmapDisplayer(300)) // 是否图片加载好后渐入的动画时间,有可能引起图片加载闪烁
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
