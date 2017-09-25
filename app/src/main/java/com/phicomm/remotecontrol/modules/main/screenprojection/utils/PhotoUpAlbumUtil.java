package com.phicomm.remotecontrol.modules.main.screenprojection.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PhotoUpImageBucket;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PhotoUpImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PhotoUpAlbumUtil extends AsyncTask<Object, Object, Object> {

    final String TAG = getClass().getSimpleName();
    Context mContext;
    ContentResolver mContentResolver;
    // 缩略图列表
    HashMap<String, String> mThumbnailList = new HashMap<>();
    // 专辑列表
    List<HashMap<String, String>> mAlbumList = new ArrayList<>();
    HashMap<String, PhotoUpImageBucket> mBucketList = new HashMap<>();
    private GetAlbumList getAlbumList;

    private PhotoUpAlbumUtil() {
    }

    public static PhotoUpAlbumUtil getHelper() {
        PhotoUpAlbumUtil instance = new PhotoUpAlbumUtil();
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        if (this.mContext == null) {
            this.mContext = context;
            mContentResolver = context.getContentResolver();
        }
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor cursor = null;
        try {
            String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID,
                    Thumbnails.DATA};
            cursor = Thumbnails.queryMiniThumbnails(mContentResolver, Thumbnails.EXTERNAL_CONTENT_URI,
                    Thumbnails.MINI_KIND, projection);
            if (cursor != null) {
                getThumbnailColumnData(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 从数据库中得到缩略图
     *
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int image_id;
            String image_path;
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
            do {
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                mThumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    /**
     * 是否创建了图片集
     */
    boolean hasBuildImagesBucketList = false;

    /**
     * 得到图片集
     */
    void buildImagesBucketList() {
        Cursor cursor = null;
        // 构造缩略图索引
        getThumbnail();
        try {
            // 构造相册索引
            String columns[] = new String[]{Media._ID, Media.BUCKET_ID,
                    Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
                    Media.SIZE, Media.BUCKET_DISPLAY_NAME};
            // 得到一个游标
            cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
                    Media.DATE_MODIFIED + " desc");
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    // 获取指定列的索引
                    int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
                    int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
                    int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
                    int bucketIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);
                    /**
                     * Description:这里增加了一个判断：判断照片的名字是否合法，例如.jpg .png图片名字是不合法的，直接过滤掉
                     */
                    do {
                        if (cursor.getString(photoPathIndex).substring(
                                cursor.getString(photoPathIndex).lastIndexOf("/") + 1,
                                cursor.getString(photoPathIndex).lastIndexOf("."))
                                .replaceAll(" ", "").length() <= 0) {
                            Log.d(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex)=" + cursor.getString(photoPathIndex));
                            Log.d(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex).substring=" + cursor.getString(photoPathIndex)
                                    .substring(cursor.getString(photoPathIndex).lastIndexOf("/") + 1, cursor.getString(photoPathIndex).lastIndexOf(".")));
                        } else {
                            String _id = cursor.getString(photoIDIndex);
                            String path = cursor.getString(photoPathIndex);
                            String bucketName = cursor.getString(bucketDisplayNameIndex);
                            String bucketId = cursor.getString(bucketIdIndex);
                            PhotoUpImageBucket bucket = mBucketList.get(bucketId);
                            if (bucket == null) {
                                bucket = new PhotoUpImageBucket();
                                mBucketList.put(bucketId, bucket);
                                bucket.mImageList = new ArrayList<>();
                                bucket.mBucketName = bucketName;
                            }
                            bucket.mCount++;
                            PhotoUpImageItem imageItem = new PhotoUpImageItem();
                            imageItem.setmImageId(_id);
                            imageItem.setmImagePath(path);
                            bucket.mImageList.add(imageItem);
                            Log.i(TAG, "PhotoUpAlbumHelper类中 的——》path=" + mThumbnailList.get(_id));
                        }
                    } while (cursor.moveToNext());
                }
            }
            hasBuildImagesBucketList = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 得到图片集
     *
     * @param refresh
     * @return
     */
    public List<PhotoUpImageBucket> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        List<PhotoUpImageBucket> tmpList = new ArrayList<>();
        Iterator<Map.Entry<String, PhotoUpImageBucket>> itr = mBucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, PhotoUpImageBucket> entry = (Map.Entry<String, PhotoUpImageBucket>) itr
                    .next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    public void destoryList() {
        mThumbnailList.clear();
        mThumbnailList = null;
        mAlbumList.clear();
        mAlbumList = null;
        mBucketList.clear();
        mBucketList = null;
    }

    public void setGetAlbumList(GetAlbumList getAlbumList) {
        this.getAlbumList = getAlbumList;
    }

    public interface GetAlbumList {
        void getAlbumList(List<PhotoUpImageBucket> list);
    }

    @Override
    protected Object doInBackground(Object... params) {
        return getImagesBucketList((Boolean) (params[0]));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        getAlbumList.getAlbumList((List<PhotoUpImageBucket>) result);
    }

}
