package com.phicomm.remotecontrol.modules.main.screenprojection.contract;

import com.phicomm.remotecontrol.base.BasePresenter;
import com.phicomm.remotecontrol.base.BasicView;

/**
 * Created by kang.sun on 2017/10/27.
 */

public class VideoContentContract {
    public interface VideoContentView extends BasicView {
        void showCheckDialog();

        void dimissCheckDialog();
    }

    public interface VideoContentPresenter extends BasePresenter {
        void checkTargetState(String ipAddress);
    }

}
