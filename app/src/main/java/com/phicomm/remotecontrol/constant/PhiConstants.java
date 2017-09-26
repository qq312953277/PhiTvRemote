package com.phicomm.remotecontrol.constant;

/**
 * Created by chunya02.li on 2017/6/23.
 */

public class PhiConstants {
    public static final String KEY_BSSID = "bssid";
    public static final String KEY_NAME = "name";
    public static final String REMOTE_SERVICE_TYPE_JMDNS = "_phibox._tcp.local.";

    public static final String SPINNER_DEVICES_LIST = "spinner_devices_list";
    public static final long DISCOVERY_TIMEOUT = 10000;  //10s

    public static final int DISCOVERY_DERVICE_REMOVE = 2000;
    public static final int BROADCAST_TIMEOUT = 1000;

    public static final String ACTION_BAR_NAME = "action_bar_name";
    public static final int SLIDE_RIGHT = 0;
    public static final int SLIDE_LEFT = 1;
    public static final int SLIDE_DOWN = 2;
    public static final int SLIDE_UP = 3;
    // ota
    public static String APP_NAME = "PhiTvRemote";
    public static final String APP_ID = "2017060031";

    //login constant
    public static final String CLIENT_ID = "5937751";
    public static final String RESPONSE_TYPE = "code"; //固定
    public static final String REQUEST_SOURCE = "5937751";
    public static final String SCOPE = "write";
    public static final String CLIENT_SECRET = "AC08CBDA7AD99831A16C8BA9353854E7";
    public static final String SMS_VERIFICATION = "0";//短信接收

    //H5
    public static final String APP_USER_REGISTER_PROTOCAL_BASE_URL = "http://ihome.phicomm.com:10011/balance-app/h5/user/register/protocol/v1/";

    //TitleBar height
    public static final int TITLE_BAR_HEIGHT_DP = 40;//单位dp
}
