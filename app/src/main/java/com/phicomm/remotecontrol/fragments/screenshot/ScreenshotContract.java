package com.phicomm.remotecontrol.fragments.screenshot;

import android.graphics.drawable.Drawable;

import com.phicomm.remotecontrol.util.BasePresenter;
import com.phicomm.remotecontrol.util.BaseView;

/**
 * Created by xufeng02.zhou 2017/7/18
 */

public interface ScreenshotContract {
    interface Presenter extends BasePresenter {
        void setView(View view);

        void doScreenshot();

        void savePicture();
    }

    interface View extends BaseView<Presenter> {
        void showPicture(Drawable drawable);
    }
}
