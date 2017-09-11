package com.phicomm.remotecontrol.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by yong04.zhou on 2017/9/11.
 */

public class VibrateUtil {

    private static Vibrator vibrator;

    public static void vibrate(Context context, int millisecond) {
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(millisecond);
    }
}
