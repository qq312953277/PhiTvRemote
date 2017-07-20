package com.phicomm.remotecontrol.fragments.controlpanel;

import com.phicomm.remotecontrol.util.BasePresenter;
import com.phicomm.remotecontrol.util.BaseView;

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
        void toastMessage();
    }
}
