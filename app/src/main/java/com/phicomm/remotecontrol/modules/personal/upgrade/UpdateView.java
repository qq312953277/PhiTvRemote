package com.phicomm.remotecontrol.modules.personal.upgrade;

import com.phicomm.remotecontrol.base.BasicView;
import com.phicomm.remotecontrol.modules.personal.upgrade.UpdateInfoResponseBean;

import butterknife.BindView;

/**
 * Created by hao04.wu on 2017/9/12.
 */

public interface UpdateView extends BasicView {
    void checkVersion(UpdateInfoResponseBean bean);
}
