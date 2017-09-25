package com.phicomm.remotecontrol.modules.main.screenprojection.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import org.fourthline.cling.support.model.ProtocolInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by kang.sun on 2017/8/22.
 */
public class FiletypeUtil {
    public static final int FILETYPE_MUSIC = 0;
    public static final int FILETYPE_PIC = 1;
    public static final int FILETYPE_MOVIE = 2;
    public static final int FILETYPE_OTHER = 3;

    /**
     * 判断文件类型
     */
    public static int getFiletype(ProtocolInfo pInfo) {
        String contentFormat = pInfo.getContentFormat();
        return getFiletype(contentFormat);
    }

    /**
     * 判断文件类型
     */
    public static int getFiletype(String contentFormat) {
        String[] types = contentFormat.split("/");
        if (types.length >= 0) {
            if ("audio".equals(types[0])) {
                return FILETYPE_MUSIC;
            }
            if ("video".equals(types[0])) {
                return FILETYPE_MOVIE;
            }
            if ("image".equals(types[0])) {
                return FILETYPE_PIC;
            }
        }
        return FILETYPE_OTHER;
    }

    /**
     * 根据文件类型获取文件的mime类型
     */
    public static String getMIMEType(int filetype) {
        String mimeType = "*/*";
        switch (filetype) {
            case FILETYPE_MUSIC:
                mimeType = "audio/*";
                break;
            case FILETYPE_PIC:
                mimeType = "image/*";
                break;
            case FILETYPE_MOVIE:
                mimeType = "video/*";
                break;
        }
        return mimeType;
    }

    /**
     * 根据url来判断文件类型
     */
    public static int getResoucesFiletype(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (isMovieType(url)) {
                return FILETYPE_MOVIE;
            }
            if (isPicType(url)) {
                return FILETYPE_PIC;
            }
        }
        return FILETYPE_OTHER;
    }

    /**
     * 判断文件是否是视频文件
     */
    public static boolean isMovieType(String url) {
        String mRes = url.toUpperCase(Locale.CHINA);
        if (mRes.endsWith(".WMV") ||
                mRes.endsWith(".ASF") ||
                mRes.endsWith(".ASX") ||
                mRes.endsWith(".RM") ||
                mRes.endsWith(".RMVB") ||
                mRes.endsWith(".MPG") ||
                mRes.endsWith(".MPEG") ||
                mRes.endsWith(".MPE") ||
                mRes.endsWith(".3GP") ||
                mRes.endsWith(".MOV") ||
                mRes.endsWith(".MP4") ||
                mRes.endsWith(".MPG_PS") ||
                mRes.endsWith(".M4V") ||
                mRes.endsWith(".AVI") ||
                mRes.endsWith(".DAT") ||
                mRes.endsWith(".MKV") ||
                mRes.endsWith(".FLV") ||
                mRes.endsWith(".VOB") ||
                mRes.endsWith(".WTV")) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否是图片文件
     */
    public static boolean isPicType(String url) {
        String mRes = url.toUpperCase(Locale.CHINA);
        if (mRes.endsWith(".BMP") ||
                mRes.endsWith(".JPG") ||
                mRes.endsWith(".JPEG") ||
                mRes.endsWith(".PNG") ||
                mRes.endsWith(".GIF")) {
            return true;
        }
        return false;
    }

    public static Bitmap adjustSubBitmap(Bitmap currentImage, int targetWidth, int tarHeight, int filletDegree) {
        if (currentImage == null) {
            return currentImage;
        }
        int picWidth = currentImage.getWidth();
        int picHeight = currentImage.getHeight();
        float scaleRate = 1.0f * targetWidth / picWidth > 1.0f * tarHeight / picHeight ?
                1.0f * targetWidth / picWidth : 1.0f * tarHeight / picHeight;
        currentImage = Bitmap.createScaledBitmap(currentImage, (int) (scaleRate * picWidth),
                (int) (scaleRate * picHeight), true);
        picWidth = currentImage.getWidth();
        picHeight = currentImage.getHeight();
        Bitmap paintingBoard = Bitmap.createBitmap(targetWidth, tarHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(paintingBoard);
        canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        final RectF rectF = new RectF(0, 0, targetWidth, tarHeight);
        canvas.drawRoundRect(rectF, filletDegree, filletDegree, paint);
        int left = 0;
        int top = 0;
        int right = targetWidth < picWidth ? targetWidth : picWidth;
        int bottom = tarHeight < picHeight ? tarHeight : picHeight;
        if (picWidth > right) {
            left = (picWidth - right) / 2;
            right = left + right;
        }
        if (picHeight > bottom) {
            top = (picHeight - bottom) / 2;
            bottom = top + bottom;
        }
        final Rect src = new Rect(left, top, right, bottom);
        final Rect dst = new Rect(0, 0, targetWidth, tarHeight);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(currentImage, src, dst, paint);
        return paintingBoard;
    }

    public static String RemoveType(File file) {
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos == -1) {
            pos = fileName.length();
        }
        fileName = fileName.substring(0, pos);
        return fileName;
    }

    public static String timeParse(long duration) {
        Date mVideoDate = new Date(duration);
        String mTimes = "";
        try {
            SimpleDateFormat mSDF = new SimpleDateFormat("HH:mm:ss");
            mSDF.setTimeZone(TimeZone.getTimeZone("GMT"));
            mTimes = mSDF.format(mVideoDate);
            mTimes = "▶ " + mTimes;
            return mTimes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
