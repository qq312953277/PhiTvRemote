package com.phicomm.remotecontrol.modules.main.screenprojection.callback;

import android.os.AsyncTask;
import android.widget.SeekBar;
import android.widget.TextView;

import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PositionInfo;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaControlBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaControlBiz.GetPositionInfoListerner;
import com.phicomm.remotecontrol.util.LogUtil;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;

/**
 * Created by kang.sun on 2017/8/23.
 */
public class RealtimeUpdatePositionInfo extends AsyncTask<Void, PositionInfo, PositionInfo> {
    public static final int SEEKBARMAX = 100;
    public static final int RESPONDRATE = 250;
    private static String TAG = RealtimeUpdatePositionInfo.class.getSimpleName();
    private boolean isPlaying; // 是否正在播放
    private SeekBar sbPlayback;
    private TextView tvCurTime;
    private TextView tvTotalTime;
    private MediaControlBiz controlBiz;

    public RealtimeUpdatePositionInfo(MediaControlBiz controlBiz, SeekBar sbPlayback, TextView tvCurTime, TextView tvTotalTime) {
        this.sbPlayback = sbPlayback;
        this.tvCurTime = tvCurTime;
        this.tvTotalTime = tvTotalTime;
        this.controlBiz = controlBiz;
        sbPlayback.setMax(SEEKBARMAX);
        isPlaying = true;
    }

    @Override
    protected PositionInfo doInBackground(Void... params) {
        PositionInfo info = null;
        while (!isCancelled()) {
            if (!isPlaying) {
                //LogUtil.d(TAG,"点击暂停后，不在接收被投屏设备返回的播放信息");
            }
            while (isPlaying) {
                //LogUtil.d(TAG,"开始接收被投屏设备返回的播放信息");
                try {
                    Thread.sleep(RESPONDRATE);
                    controlBiz.getPositionInfo(new GetPositionInfoListerner() {
                        @Override
                        public void onSuccess(PositionInfo positionInfo) {
                            publishProgress(positionInfo);
                            LogUtil.d(TAG, "!!!!被投屏设备返回的tvCurTime信息是：" + positionInfo.getRelTime());
                        }

                        @Override
                        public void failure(ActionInvocation invocation, UpnpResponse operation,
                                            String defaultMsg) {
                            LogUtil.d(TAG, "Get position info failure:" + defaultMsg);
                        }
                    });
                } catch (InterruptedException e) {
                    LogUtil.d(TAG, "Get position info failure:" + e.getMessage());
                }
            }
        }
        return info;
    }

    @Override
    protected void onProgressUpdate(PositionInfo... values) {
        PositionInfo info = values[0];
        sbPlayback.setProgress(info.getElapsedPercent());
        tvCurTime.setText(info.getRelTime());
        tvTotalTime.setText(info.getTrackDuration());
        LogUtil.d(TAG, "开始设置tvCurTime：" + info.getRelTime() + ",tvTotalTime:" + info.getTrackDuration());
//        if(isPlayingOver(info.getRelTime(),info.getTrackDuration())){
//            isPlaying = false;
//            onCancelled();
//        }
    }

    private boolean isPlayingOver(String relTime, String trackDuration) {
        String[] relTimeArr = relTime.split(":");
        String[] tolTimeArr = trackDuration.split(":");
        LogUtil.d(TAG, "relTimeArr" + Integer.parseInt(relTimeArr[2]));
        LogUtil.d(TAG, "tolTimeArr" + Integer.parseInt(tolTimeArr[2]));
        if ((relTimeArr[0].equals(tolTimeArr[0])) && (relTimeArr[1].equals(tolTimeArr[1])) && (Integer.parseInt(tolTimeArr[2]) - Integer.parseInt(relTimeArr[2]) < 4)) {
            LogUtil.d(TAG, "还有4秒");
            return true;
        }
        return false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 设置是否暂停
     */
    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        sbPlayback = null;
        tvCurTime = null;
        tvTotalTime = null;
        controlBiz = null;
    }
}

