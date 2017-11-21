package com.phicomm.remotecontrol.modules.main.screenprojection.callback;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import com.phicomm.remotecontrol.modules.main.screenprojection.entity.PositionInfo;
import com.phicomm.remotecontrol.modules.main.screenprojection.event.SetSeekBarStateEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.event.StopVideoEvent;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaControlBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaControlBiz.GetPositionInfoListerner;
import com.phicomm.remotecontrol.modules.main.screenprojection.utils.DurationUtil;
import com.phicomm.remotecontrol.util.LogUtil;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.greenrobot.eventbus.EventBus;

import static com.phicomm.remotecontrol.modules.main.screenprojection.utils.DurationUtil.convertToSeconds;

/**
 * Created by kang.sun on 2017/8/23.
 */
public class RealtimeUpdatePositionInfo extends AsyncTask<Void, PositionInfo, PositionInfo> {
    private static String TAG = "RealtimeUpdatePositionInfo";
    public static final int SEEKBARMAX = 100;
    public static final int RESPONDRATE = 250;
    public static final int DELAYTIME = 4;
    public static final String INITTIME = "00:00:00";
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
            while (isPlaying) {
                try {
                    Thread.sleep(RESPONDRATE);
                    controlBiz.getPositionInfo(new GetPositionInfoListerner() {
                        @Override
                        public void onSuccess(PositionInfo positionInfo) {
                            publishProgress(positionInfo);
                            if (!INITTIME.equals(positionInfo.getRelTime())) {
                                EventBus.getDefault().post(new SetSeekBarStateEvent(true));
                            }
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
        checkVideoIsOver(info.getRelTime(), info.getTrackDuration());
    }

    private void checkVideoIsOver(String relTime, String trackDuration) {
        if (!(TextUtils.isEmpty(relTime) || TextUtils.isEmpty(trackDuration))) {
            int mRelTime = DurationUtil.convertToSeconds(relTime);
            int mTotalTime = DurationUtil.convertToSeconds(trackDuration);
            if ((mTotalTime != 0) && (mRelTime != 0) && (mTotalTime - mRelTime <= DELAYTIME)) {
                EventBus.getDefault().post(new StopVideoEvent(true));
            }
        }
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

