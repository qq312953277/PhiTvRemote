package com.phicomm.remotecontrol.constant;

import com.phicomm.remotecontrol.BuildConfig;

/**
 * Created by xufeng02.zhou on 2017/7/19.
 */

public class Configs {
    public static String LOG_TAG = BuildConfig.LOG_TAG;
    public final static boolean DEBUG = BuildConfig.LOG_DEBUG;
    public final static int DEFAULT_PORT = 8080;

    public final static String SCRENSHOT_DIR = "PhiboxScreenshot";
    public final static String SCRENSHOT_PREFIX = "phibox_";
    public final static String SCRENSHOT_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    public final static String SCRENSHOT_SUFFIX = ".jpg";
}
