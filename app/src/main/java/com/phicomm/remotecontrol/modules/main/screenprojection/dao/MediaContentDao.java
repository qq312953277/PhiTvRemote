package com.phicomm.remotecontrol.modules.main.screenprojection.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;

import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentTree;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ImageItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.MItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.VideoItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.utils.DurationUtil;

import org.fourthline.cling.support.model.Res;
import org.seamless.util.MimeType;

import java.util.ArrayList;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class MediaContentDao {
    private static String mResAddress;
    private static ContentResolver mContentResolver;

    public MediaContentDao(Context ctx, String serverAdd) {
        mContentResolver = ctx.getContentResolver();
        mResAddress = "http://" + serverAdd + "/";
    }

    public ArrayList<MItem> getImageItems() {
        ArrayList<MItem> items = new ArrayList<>();
        String[] imageColumns = {Images.Media._ID,
                Images.Media.TITLE,
                Images.Media.DATA,
                Images.Media.MIME_TYPE,
                Images.Media.SIZE};
        Cursor cur = mContentResolver.query(Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, null);
        if (cur == null) {
            return items;
        }
        try {
            while (cur.moveToNext()) {
                String id = ContentTree.IMAGE_PREFIX + cur.getInt(cur.getColumnIndex(MediaStore.Images.Media._ID));
                String creator = "unkown";
                String filePath = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
                String tittle = getPicNameFromPath(filePath);
                String mimeType = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                long size = cur.getLong(cur
                        .getColumnIndex(MediaStore.Images.Media.SIZE));
                Res res = new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
                        mimeType.substring(mimeType.indexOf('/') + 1)), size, mResAddress + id);
                ImageItem imageItem = new ImageItem(id, ContentTree.IMAGE_ID,
                        tittle, creator, filePath, res);
                items.add(imageItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cur.close();
        }
        return items;
    }

    public ArrayList<MItem> getVideoItems() {
        ArrayList<MItem> items = new ArrayList<>();
        String[] videoColumns = {Video.Media._ID,
                Video.Media.TITLE,
                Video.Media.DATA,
                Video.Media.ARTIST,
                Video.Media.MIME_TYPE,
                Video.Media.SIZE,
                Video.Media.DURATION,
                Video.Media.RESOLUTION};
        Cursor cur = mContentResolver.query(Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
        if (cur == null) {
            return items;
        }
        try {
            while (cur.moveToNext()) {
                String id = ContentTree.VIDEO_PREFIX + cur.getInt(cur.getColumnIndex(Video.Media._ID));
                String filePath = cur.getString(cur.getColumnIndex(Video.Media.DATA));
                String tittle = getPicNameFromPath(filePath);
                String creator = cur.getString(cur.getColumnIndex(Video.Media.ARTIST));
                String mimeType = cur.getString(cur.getColumnIndex(Video.Media.MIME_TYPE));
                long size = cur.getLong(cur.getColumnIndex(Video.Media.SIZE));
                long duration = cur.getLong(cur.getColumnIndex(Video.Media.DURATION));
                String resolution = cur.getString(cur.getColumnIndex(Video.Media.RESOLUTION));
                Res res = new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
                        mimeType.substring(mimeType.indexOf('/') + 1)), size, mResAddress + id);
                res.setDuration(DurationUtil.toMilliTimeString(duration));
                res.setResolution(resolution);
                VideoItem videoItem = new VideoItem(id, ContentTree.VIDEO_ID, tittle, creator, filePath, res);
                items.add(videoItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cur.close();
        }
        return items;
    }

    public ArrayList<MItem> getImageItems(String mAlbumName) {
        ArrayList<MItem> items = new ArrayList<MItem>();
        String[] imageColumns = {Images.Media._ID,
                Images.Media.TITLE,
                Images.Media.DATA,
                Images.Media.MIME_TYPE,
                Images.Media.SIZE,
                Images.Media.BUCKET_DISPLAY_NAME};
        Cursor cur = mContentResolver.query(Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, Images.Media.DATE_MODIFIED + " desc");
        if (cur == null) {
            return items;
        }
        try {
            while (cur.moveToNext()) {
                String mBucketName = cur.getString(cur.getColumnIndexOrThrow(Images.Media.BUCKET_DISPLAY_NAME));
                if (mBucketName.equals(mAlbumName)) {
                    String id = ContentTree.IMAGE_PREFIX + cur.getInt(cur.getColumnIndex(MediaStore.Images.Media._ID));
                    String creator = "unkown";
                    String filePath = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
                    String tittle = getPicNameFromPath(filePath);
                    String mimeType = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                    long size = cur.getLong(cur
                            .getColumnIndex(MediaStore.Images.Media.SIZE));
                    Res res = new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
                            mimeType.substring(mimeType.indexOf('/') + 1)), size, mResAddress + id);
                    ImageItem imageItem = new ImageItem(id, ContentTree.IMAGE_ID,
                            tittle, creator, filePath, res);
                    items.add(imageItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cur.close();
        }
        return items;
    }

    public String getPicNameFromPath(String picturePath) {
        String temp[] = picturePath.replaceAll("\\\\", "/").split("/");
        String fileName = "";
        if (temp.length > 1) {
            fileName = temp[temp.length - 1];
        }
        return fileName;
    }
}


