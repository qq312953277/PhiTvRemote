package com.phicomm.remotecontrol.modules.main.controlpanel;

import com.phicomm.remotecontrol.base.BasePresenter;
import com.phicomm.remotecontrol.base.BaseView;

/**
 * Created by hzn on 17-5-11.
 */

public interface PanelContract {
    interface Presenter extends BasePresenter {
        void setView(View view);

        void sendKeyEvent(int keyCode);

        void sendKeyLongClickEvent(int keyCode);

        void sendCommand(String cmd);
    }

    interface View extends BaseView<Presenter> {
        void toastMessage(String msg);
//        void connectFail();
    }
}
