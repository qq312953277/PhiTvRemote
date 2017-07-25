package com.phicomm.remotecontrol.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Author: xufeng02.zhou
 * Date  : 2017-06-30
 * last modified: 2017-06-30
 */
public class Status {
    String name;
    String sn;
    @SerializedName("online")
    long onLine;
    @SerializedName("strength")
    int wifiStrength;

    public Status(String name, String sn, int strength, long online) {
        this.name = name;
        this.sn = sn;
        wifiStrength = strength;
        onLine = online;
    }

    public String getName() {
        return name;
    }

    public String getSn() {
        return sn;
    }

    public long getOnLine() {
        return onLine;
    }

    public int getWifiStrength() {
        return wifiStrength;
    }
}

