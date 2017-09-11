package com.phicomm.remotecontrol.modules.main.screenprojection.contract;

import com.phicomm.remotecontrol.base.BasicView;

/**
 * Created by kang.sun on 2017/9/11.
 */

public class VideoControlContract {

    public interface VideoControlView extends BasicView {
        void fromPlayToPause();

        void fromPauseToPlay();
    }

    public interface VideoControlPresenter {
        void play();

        void playVideo();

        void pauseVideo();

        void seekVideo(String totalTime, int prog);

        void destroy();

        void updatePlayingState(boolean isPlaying);

        void showMessage(Object message);
    }
}
