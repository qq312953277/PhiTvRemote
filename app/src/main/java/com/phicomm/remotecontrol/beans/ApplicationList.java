package com.phicomm.remotecontrol.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Author: allen.z
 * Date  : 2017-07-03
 * last modified: 2017-07-03
 */
public class ApplicationList {
    @SerializedName("count")
    int mCount;
    @SerializedName("apps")
    ArrayList<ApplicationInfo> mApps;

    public ApplicationList(int length, ArrayList<ApplicationInfo> list) {
        mCount = length;
        mApps = list;
    }
}
