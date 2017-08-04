package com.phicomm.remotecontrol.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {


    public static boolean isNull(String s) {
        if (s == null || "".equals(s) || "null".equals(s)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 16进制的字符数组
     */
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    /**
     * 字符串是否为空
     *
     * @param txt
     * @return
     */
    public static boolean isBlank(String txt) {
        return TextUtils.isEmpty(txt);
    }

    /***
     * 时间戳格式化时间串
     *
     * @param seconds 时间戳,以秒为单位
     * @return 格式化后的字符串
     */
    public static String timestamp2FormatBySeconds(long seconds) {
        return timestamp2FormatByMilliSeconds(seconds * 1000);
    }

    /***
     * 时间戳格式化时间串
     *
     * @param ms 时间戳,以毫秒为单位
     * @return 格式化后的字符串
     */
    @SuppressLint("SimpleDateFormat")
    public static String timestamp2FormatByMilliSeconds(long ms) {

        Date date = new Date(ms);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 时间戳格式化，仅保留年月日
     *
     * @param millis
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String timeFomateDate(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd").format(millis);
    }

    /**
     * 格式化时间转长整型时间戳
     *
     * @param format
     * @param time
     * @return 时间戳数值
     */
    @SuppressLint("SimpleDateFormat")
    public static long timeFormat2Timestamp(String format, String time) {
        long ret = 0;
        try {
            ret = new SimpleDateFormat(format).parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 格式化时间转长整型时间戳 默认时间格式为：yyyy-MM-dd HH:mm:ss,其他格式请使用
     * {@link}转换
     *
     * @param time 格式化时间
     * @return 时间戳数值
     */
    public static long timeFormat2Timestamp(String time) {
        return timeFormat2Timestamp("yyyy-MM-dd HH:mm:ss", time);
    }

    /**
     * 将整形时间长度格式化为字符串HH:mm:ss的形式 当HH为0时，显示为mm:ss的形式
     *
     * @param timeLength
     * @return
     */
    public static String formatTimeLength(int timeLength) {
        StringBuffer voiceLength = new StringBuffer("");
        int divideResult;
        int modResult;

        // 计算HH
        divideResult = timeLength / 3600;
        modResult = timeLength % 3600;
        if (divideResult > 0) {
            voiceLength.append(divideResult + ":");
        }

        // 计算mm
        divideResult = modResult / 60;
        modResult = modResult % 60;
        if (divideResult / 10 > 0) {
            voiceLength.append(divideResult + ":");
        } else {
            voiceLength.append("0").append(divideResult).append(":");
        }

        // 计算ss
        modResult = modResult % 60;
        if (modResult / 10 > 0) {
            voiceLength.append(modResult);
        } else {
            voiceLength.append("0").append(modResult);
        }
        return voiceLength.toString();
    }

    /**
     * 判断参数内容是否是手机号码，目前只认13* 14* 15* 17* 18*开头的手机号
     *
     * @param content
     * @return true or false
     */
    public static boolean isPhoneNum(String content) {
        boolean ret = false;
        if (!TextUtils.isEmpty(content)) {
            ret = content.matches("^1[3|4|5|7|8][0-9]\\d{8}$");
        }
        return ret;
    }

    /**
     * 判断字符串是否是邮箱地址
     *
     * @param content
     * @return true or false
     */
    public static boolean isEmail(String content) {
        boolean ret = false;
        if (!TextUtils.isEmpty(content)) {
            ret = content.matches("^(?=\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$).{6,30}$");
        }
        return ret;
    }

    /**
     * 判断字符串是否是邮箱地址
     *
     * @param content
     * @return true or false
     */
    public static boolean isRouterEmail(String content) {
        boolean ret = false;
        if (!TextUtils.isEmpty(content)) {
            ret = content.matches("^(?=\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$).{6,64}$");
        }
        return ret;
    }

    public static String formatSize(long size) {
        String sSzie;
        size = size >> 10; // size /= 1024 kb
        if (size < 1024) {
            sSzie = String.format("%d Kb", size);
        } else {
            if (size >> 10 < 1024) { // MB
                sSzie = String.format("%.2f M", size / 1024d);// mb
            } else {
                sSzie = String.format("%.2f GB", size / 2048d);// gb
            }
        }
        return sSzie;
    }

    /**
     * @param patternString 判断条件，为正则表达式
     * @param input         需要进行判断的字符串
     * @return true，符合给定条件；false，不符合给定条件
     */
    public static boolean islegal(String patternString, String input) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取IP地址中每段的数值,也可用于对判断IP地址是否合法，如果返回值为null，则表示是非法的IP地址
     *
     * @param input
     * @return
     */
    public static int[] getIpSplit(String input) {
        if (input == null) {
            return null;
        }
        // if ( input.startsWith(".") || input.endsWith(".") )
        // {
        // return null;
        // }

        String[] numbers = input.split("\\.");
        int length = numbers.length;
        if (length != 4) {
            return null;
        }

        int[] ipSplits = new int[4];

        for (int i = 0; i < length; i++) {
            try {
                ipSplits[i] = Integer.parseInt(numbers[i]);
                if (ipSplits[i] < 0 || ipSplits[i] > 255) {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        return ipSplits;
    }


    /**
     * 字符串是否包含大写字母
     *
     * @param input
     * @return
     */
    public static boolean isContainCaptalLetter(String input) {
        Pattern p = Pattern.compile("[\\x41-\\x5a]");// [\\x00-\\x7f]
        // 判断ascii码
        Matcher m = p.matcher(input);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 字符串是否包含小写字母
     *
     * @param input
     * @return
     */
    public static boolean isContainLowerCapital(String input) {
        Pattern p = Pattern.compile("[\\x61-\\x7a]");// [\\x00-\\x7f]
        // 判断ascii码
        Matcher m = p.matcher(input);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 字符串是否包含数字
     *
     * @param input
     * @return
     */
    public static boolean isContainNumber(String input) {
        Pattern p = Pattern.compile("[\\x30-\\x39]");// [\\x00-\\x7f]
        // 判断ascii码
        Matcher m = p.matcher(input);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static long getNumFromString(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String numStr = m.replaceAll("").trim();
        if (TextUtils.isEmpty(numStr)) {
            numStr = "0";
        }
        return Long.parseLong(numStr);
    }

    /**
     * 字符串是否包含空格等字符
     *
     * @param input
     * @return
     */
    public static boolean isContainBlankSpace(String input) {
        Pattern p = Pattern.compile("[\\s]");// [\\x00-\\x7f]
        // 判断ascii码
        Matcher m = p.matcher(input);
        if (m.find()) {
            return true;
        }
        return false;
    }


    public static boolean isContainChinese(String str) {
        //CHECKSTYLE:OFF
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        //CHECKSTYLE:ON
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 获取md5加密后的结果
     *
     * @param content
     * @return
     */
    public static String getMD5(String content) {
        return MD5Encode(content, true);
    }

    /**
     * @param source    需要加密的原字符串
     * @param uppercase 是否转为大写字符串
     * @return
     */
    public static String MD5Encode(String source, boolean uppercase) {
        String result = null;
        try {
            result = source;
            // 获得MD5摘要对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用指定的字节数组更新摘要信息
            messageDigest.update(result.getBytes());
            // messageDigest.digest()获得16位长度
            result = byteArrayToHexString(messageDigest.digest());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uppercase ? result.toUpperCase() : result;
    }

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

    /**
     * 去掉所有空格
     *
     * @param str
     * @return
     */
    public static String removeAllSpace(String str) {
        String result;
        result = str.replace(" ", "");
        return result;
    }

    /**
     * 判断字符串是否为空
     *
     * @param input 字符串
     * @return true为空，false不为空
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input.trim()) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证手机号是否合法
     *
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        return p.matcher(mobile).matches();
    }

    /**
     * 验证验证码是否合法
     */
    public static boolean checkVerifyCode(String verify_code) {
        Pattern p = Pattern.compile("^\\d{6}$"); // 检查验证码位数
        return p.matcher(verify_code).matches();
    }

    public static boolean checkCard(String card) {
        if (isEmpty(card)) {
            return false;
        } else if (card.length() == 18) {
            Pattern pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
            return pattern.matcher(card).matches();
        } else if (card.length() == 15) {
            Pattern pattern = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
            return pattern.matcher(card).matches();
        }
        return false;
    }

    /**
     * 判断密码长度 [6,20]
     *
     * @param psd
     */
    public static boolean checkPsdLength(String psd) {
        if (isEmpty(psd)) {
            if (psd.length() > 16 || psd.length() < 6) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static String replace(String string, String oldChar, String newChar) {
        if (!StringUtils.isNull(string) && string.contains(oldChar)) {
            string = string.replace(oldChar, newChar);
        }
        return string;
    }
}
