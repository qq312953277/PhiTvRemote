package com.phicomm.remotecontrol.modules.main.screenprojection.utils;

import android.text.TextUtils;

import org.fourthline.cling.support.model.ProtocolInfo;

import java.util.Locale;

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
}
