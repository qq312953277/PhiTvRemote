package com.phicomm.remotecontrol.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Author: xufeng02.zhou
 * Date  : 2017-06-30
 * last modified: 2017-06-30
 */
public class ApplicationInfo {
    @SerializedName("name")
    String name;
    @SerializedName("package")
    String packageName;
    @SerializedName("activity")
    String className;
    @SerializedName("appid")
    String appid;

    public ApplicationInfo(String name, String packageName, String className, String appid) {
        this.name = name;
        this.packageName = packageName;
        this.className = className;
        this.appid = appid;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;

    }
}
