package com.phicomm.remotecontrol.modules.main.screenprojection.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.constant.KeyCode;
import com.phicomm.remotecontrol.modules.main.controlpanel.PanelContract;
import com.phicomm.remotecontrol.modules.main.controlpanel.PanelPresenter;
import com.phicomm.remotecontrol.modules.main.screenprojection.contract.VideoControlContract;
import com.phicomm.remotecontrol.modules.main.screenprojection.presenter.VideoControlPresenterImpl;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kang.sun on 2017/8/23.
 */
public class VideoControlActivity extends BaseActivity implements VideoControlContract.VideoControlView {
    private static String TAG = "VideoControlActivity";
    private VideoControlContract.VideoControlPresenter mVideoControlPresenter;
    private PanelContract.Presenter mPresenter;
    private boolean canSeeking;
    @BindView(R.id.ib_play)
    ImageButton ibPlay;
    @BindView(R.id.ib_pause)
    ImageButton ibPause;
    @BindView(R.id.tv_totalTime)
    TextView tvTotalTime;
    @BindView(R.id.tv_curTime)
    TextView tvCurTime;
    @BindView(R.id.sb_playback)
    SeekBar sbPlayback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediacontrol);
        mVideoControlPresenter = new VideoControlPresenterImpl(this, (BaseApplication) getApplication(),
                sbPlayback, tvTotalTime, tvCurTime);
        canSeeking = true;
        mPresenter = new PanelPresenter(this);
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoControlPresenter.destroy();
    }

    private void setListener() {
        sbPlayback.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int mSeekBarProg = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (canSeeking) {
                    int prog = seekBar.getProgress();
                    String totalTime = tvTotalTime.getText().toString();
                    mVideoControlPresenter.seekVideo(totalTime, prog);
                } else {
                    seekBar.setProgress(mSeekBarProg);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (canSeeking) {
                    LogUtil.d(TAG, "移动seekbar之前先让其暂停");
                    mVideoControlPresenter.updatePlayingState(false);
                } else {
                    mSeekBarProg = seekBar.getProgress();
                    mVideoControlPresenter.showMessage("视频已暂停，不可拖动");
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtil.d(TAG,"移动seekbar之前先让其暂停");
            }
        });
    }

    @OnClick({R.id.ib_play, R.id.ib_pause})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_play:
                canSeeking = true;
                LogUtil.d(TAG,"播放按钮被点击，canSeeking");
                mVideoControlPresenter.updatePlayingState(true);
                mPresenter.sendKeyEvent(KeyCode.CENTER);
                break;
            case R.id.ib_pause:
                canSeeking = false;
                LogUtil.d(TAG,"暂停按钮被点击，notCanSeeking");
                mVideoControlPresenter.updatePlayingState(false);
                mPresenter.sendKeyEvent(KeyCode.CENTER);
                break;
        }
    }

    @Override
    public void showMessage(Object message) {
        CommonUtils.showToastBottom((String) message);
    }

    @Override
    public void onSuccess(Object message) {
    }

    @Override
    public void onFailure(Object message) {
    }

    @Override
    public void fromPlayToPause() {
        ibPlay.setVisibility(View.GONE);
        ibPause.setVisibility(View.VISIBLE);
    }

    @Override
    public void fromPauseToPlay() {
        ibPlay.setVisibility(View.VISIBLE);
        ibPause.setVisibility(View.GONE);
    }
}
