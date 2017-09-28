package com.phicomm.remotecontrol.modules.personal.apply;

import com.phicomm.remotecontrol.beans.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hao04.wu on 2017/9/18.
 */

public class ApplyInfosBean extends BaseResponseBean {
    public int count;
    public List<AppInfo> apps;

    public class AppInfo implements Serializable {
        public String appid;
        public String activity;
        public String name;
    }
}
