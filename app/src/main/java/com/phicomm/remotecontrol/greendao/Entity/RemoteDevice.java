package com.phicomm.remotecontrol.greendao.Entity;

import com.phicomm.remotecontrol.beans.BaseResponseBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by chunya02.li on 2017/6/30.
 */
@Entity
public class RemoteDevice implements Serializable {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String address;
    private int port;
    private String bssid;
    private long time;

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getBssid() {
        return this.bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId(Long id) {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    @Keep
    public RemoteDevice(Long id, String name, String address, int port, String bssid,
                        long time) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.port = port;
        this.bssid = bssid;
        this.time = time;
    }

    @Keep
    public RemoteDevice() {
    }
}
