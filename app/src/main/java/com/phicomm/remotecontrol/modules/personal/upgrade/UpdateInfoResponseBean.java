package com.phicomm.remotecontrol.modules.personal.upgrade;

import com.phicomm.remotecontrol.beans.BaseResponseBean;

/**
 * Created by hk on 2016/10/11.
 */
public class UpdateInfoResponseBean extends BaseResponseBean {
    private String ret;
    private String id;
    private String verType;
    private String verCode;
    private String verName;
    private String verTime;
    private String verInfos;
    private String verDown;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVerType() {
        return verType;
    }

    public void setVerType(String verType) {
        this.verType = verType;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getVerTime() {
        return verTime;
    }

    public void setVerTime(String verTime) {
        this.verTime = verTime;
    }

    public String getVerInfos() {
        return verInfos;
    }

    public void setVerInfos(String verInfos) {
        this.verInfos = verInfos;
    }

    public String getVerDown() {
        return verDown;
    }

    public void setVerDown(String verDown) {
        this.verDown = verDown;
    }

}
