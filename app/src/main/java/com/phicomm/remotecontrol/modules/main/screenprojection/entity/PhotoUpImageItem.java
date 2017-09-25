package com.phicomm.remotecontrol.modules.main.screenprojection.entity;

import java.io.Serializable;

public class PhotoUpImageItem implements Serializable {

    //图片ID
    private String mImageId;

    //原图路径
    private String mImagePath;

    public String getmImageId() {
        return mImageId;
    }

    public void setmImageId(String mImageId) {
        this.mImageId = mImageId;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }


}
