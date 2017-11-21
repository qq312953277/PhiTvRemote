package com.phicomm.remotecontrol.modules.main.screenprojection.utils;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class DurationUtil {
    public static String toMilliTimeString(long milliseconds) {
        milliseconds = milliseconds / 1000;
        return toTimeString(milliseconds);
    }

    public static String toTimeString(long seconds) {
        long hours = seconds / (60 * 60);
        long minutes = seconds % (60 * 60) / 60;
        long second = seconds % 60;
        return ((hours < 10 ? "0" : "") + hours
                + ":" + (minutes < 10 ? "0" : "") + minutes
                + ":" + (second < 10 ? "0" : "") + second);
    }

    public static int convertToSeconds(String time) {
        String[] timeArray = time.split(":");
        return Integer.parseInt(timeArray[0]) * 3600 + Integer.parseInt(timeArray[1]) * 60 + Integer.parseInt(timeArray[2]);
    }
}

