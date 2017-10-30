package com.phicomm.remotecontrol.modules.main.screenprojection.presenter;

import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;

import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.callback.RealtimeUpdatePositionInfo;
import com.phicomm.remotecontrol.modules.main.screenprojection.constants.MediaControlOperation;
import com.phicomm.remotecontrol.modules.main.screenprojection.constants.MediaEventType;
import com.phicomm.remotecontrol.modules.main.screenprojection.constants.TransportState;
import com.phicomm.remotecontrol.modules.main.screenprojection.contract.VideoControlContract;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.AVTransportInfo;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaControlBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaEventBiz;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.model.item.Item;

import java.util.HashMap;

/**
 * Created by kang.sun on 2017/9/1.
 */
public class VideoControlPresenterImpl implements VideoControlContract.VideoControlPresenter {
    private static String TAG = "VideoControlPresenterImpl";
    private VideoControlContract.VideoControlView mView;
    private BaseApplication mBaseApplication;
    private SeekBar sbPlayback;
    private TextView tvTotalTime;
    private TextView tvCurTime;
    private Item item;
    private Handler handler;
    private RealtimeUpdatePositionInfo realtimeUpdate;
    protected MediaControlBiz controlBiz;
    private long mId;
    private MediaEventBiz eventBiz;
    public static final int DELAY_TIME = 4000;

    public VideoControlPresenterImpl(VideoControlContract.VideoControlView mView, BaseApplication mBaseApplication,
                                     SeekBar sbPlayback, TextView tvTotalTime, TextView tvCurTime) {
        this.mView = mView;
        this.mBaseApplication = mBaseApplication;
        this.sbPlayback = sbPlayback;
        this.tvTotalTime = tvTotalTime;
        this.tvCurTime = tvCurTime;
        init();
        play();
    }

    private void init() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MediaControlOperation.PLAY:
                        updatePlayingState(true);
                        break;
                    case MediaControlOperation.PAUSE:
                        updatePlayingState(false);
                        break;
                    case MediaControlOperation.SEEK:
                        try {
                            Thread.sleep(DELAY_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        updatePlayingState(true);
                        break;
                    case MediaEventType.AV_TRANSPORT:
                        AVTransportInfo avtInfo = (AVTransportInfo) msg.obj;
                        HashMap<String, Boolean> currStates = avtInfo.getValueIsChange();
                        if (currStates.get(AVTransportInfo.CURRENT_MEDIA_DURATION)) {
                            tvTotalTime.setText(avtInfo.getCurrentMediaDuration());
                        }
                        if (currStates.get(AVTransportInfo.TRANSPORT_STATE)) {
                            String currState = avtInfo.getTransportState();
                            if (TransportState.PLAYING.equals(currState)) {
                                updatePlayingState(true);
                            }
                        }
                        break;
                }
            }
        };
        Device device = mBaseApplication.getDeviceDisplay().getDevice();
        item = mBaseApplication.getItem();
        mId = 0;
        controlBiz = new MediaControlBiz(device, handler, mId);
        eventBiz = new MediaEventBiz(device, handler);
        realtimeUpdate = new RealtimeUpdatePositionInfo(controlBiz, sbPlayback,
                tvCurTime, tvTotalTime);
        realtimeUpdate.execute();
        eventBiz.addRenderingEvent();
        eventBiz.addAVTransportEvent();
    }

    public void updatePlayingState(boolean isPlaying) {
        realtimeUpdate.setPlaying(isPlaying);
        if (isPlaying) {
            mView.fromPlayToPause();
        } else {
            mView.fromPauseToPlay();
        }
    }

    @Override
    public void showMessage(Object message) {
        mView.showMessage(message);
    }

    @Override
    public void play() {
        controlBiz.setPlayUri(item);
    }

    @Override
    public void playVideo() {
        controlBiz.play();
    }

    @Override
    public void pauseVideo() {
        controlBiz.pause();
    }

    @Override
    public void stopVideo() {
        controlBiz.stop();
    }

    @Override
    public void seekVideo(String totalTime, int prog) {
        controlBiz.seek(totalTime, prog);
    }

    @Override
    public void destroy() {
        realtimeUpdate.setPlaying(false);
        realtimeUpdate.cancel(true);
        eventBiz.removeRenderingEvent();
        eventBiz.removeAVTransportEvent();
    }
}
