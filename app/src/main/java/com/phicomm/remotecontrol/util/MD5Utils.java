package com.phicomm.remotecontrol.util;

import java.security.MessageDigest;
import java.util.Locale;

/**
 * Created by yong04.zhou on 2017/9/14.
 */

public class MD5Utils {

    /**
     * 16进制的字符数组
     */
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    /**
     * 转换字节数组为16进制字符串
     *
     * @param bytes 字节数组
     * @return
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte tem : bytes) {
            stringBuilder.append(byteToHexString(tem));
        }
        return stringBuilder.toString();
    }

    /**
     * 对字符串进行MD5加密
     *
     * @param content
     * @return
     */
    public static String encryptedByMD5(String content) {
        String result = null;
        try {
            result = content;
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(result.getBytes());
            result = byteArrayToHexString(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toUpperCase(Locale.getDefault());
    }

    /**
     * 转换byte到16进制
     *
     * @param b 要转换的byte
     * @return 16进制对应的字符
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
