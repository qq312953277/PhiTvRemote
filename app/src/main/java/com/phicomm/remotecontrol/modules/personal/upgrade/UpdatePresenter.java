package com.phicomm.remotecontrol.modules.personal.upgrade;

import java.util.Map;

/**
 * Created by hk on 2016/10/11.
 */
public interface UpdatePresenter {
    void checkVersion(Map<String, String> options);
    void downloadFile(String url,String versionName);
}
