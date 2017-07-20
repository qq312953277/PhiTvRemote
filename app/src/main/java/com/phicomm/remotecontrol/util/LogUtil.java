package com.phicomm.remotecontrol.util;


import com.phicomm.remotecontrol.constant.Configs;

/**
 * Created by xufeng02.zhou on 2017/7/17.
 */

public class LogUtil {
    private static final String TAG = Configs.LOG_TAG;
    private static final boolean LOG = Configs.DEBUG;

    public static void i(String msg) {
        if (LOG)
            android.util.Log.i(TAG, msg);
    }

    public static void i(String subtag, String msg) {
        if (LOG) {
            String tag = TAG + "/" + subtag;
            android.util.Log.i(tag, msg);
        }
    }

    public static void d(String msg) {
        if (LOG)
            android.util.Log.d(TAG, msg);
    }

    public static void d(String subtag, String msg) {
        if (LOG) {
            String tag = TAG + "/" + subtag;
            android.util.Log.d(tag, msg);
        }
    }

    public static void w(String msg) {
        if (LOG)
            android.util.Log.w(TAG, msg);
    }

    public static void w(String subtag, String msg) {
        if (LOG) {
            String tag = TAG + "/" + subtag;
            android.util.Log.w(tag, msg);
        }
    }

    public static void v(String msg) {
        if (LOG)
            android.util.Log.v(TAG, msg);
    }

    public static void v(String subtag, String msg) {
        if (LOG) {
            String tag = TAG + "/" + subtag;
            android.util.Log.v(tag, msg);
        }
    }

    public static void e(String msg) {
        android.util.Log.e(TAG, msg);
    }

    public static void e(String subtag, String msg) {
        String tag = TAG + "/" + subtag;
        android.util.Log.e(tag, msg);
    }
}
