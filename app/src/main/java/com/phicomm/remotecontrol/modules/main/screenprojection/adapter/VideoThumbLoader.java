package com.phicomm.remotecontrol.modules.main.screenprojection.adapter;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.phicomm.remotecontrol.modules.main.screenprojection.utils.FiletypeUtil;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

/**
 * Created by yong04.zhou on 2017/9/18.
 */

public class VideoThumbLoader {

    private LruCache<String, Bitmap> mLruCache;

    public VideoThumbLoader() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 4;
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void addVideoThumbToCache(String path, Bitmap bitmap) {
        if (getVideoThumbToCache(path) == null) {
            if (path != null && bitmap != null)
                mLruCache.put(path, bitmap);
        }
    }

    public Bitmap getVideoThumbToCache(String path) {
        return mLruCache.get(path);
    }

    public void showThumbByAsynctack(String path, ImageView imgview) {
        if (getVideoThumbToCache(path) == null) {
            new VideoLoaderTask(imgview, path).execute(path);
        } else {
            imgview.setImageBitmap(getVideoThumbToCache(path));
        }
    }

    class VideoLoaderTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView mImageView;
        private String mPath;

        public VideoLoaderTask(ImageView mImageView, String mPath) {
            this.mImageView = mImageView;
            this.mPath = mPath;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mPath, MINI_KIND);
            bitmap = FiletypeUtil.adjustSubBitmap(bitmap, VideoAdapter.PIC_WIDTH, VideoAdapter.PIC_HEIGHT, VideoAdapter.FIllET_DEGREE);
            if (getVideoThumbToCache(params[0]) == null) {
                addVideoThumbToCache(mPath, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mImageView.getTag().equals(mPath)) {
                if (bitmap != null)
                    mImageView.setImageBitmap(bitmap);
            }
        }
    }
}

