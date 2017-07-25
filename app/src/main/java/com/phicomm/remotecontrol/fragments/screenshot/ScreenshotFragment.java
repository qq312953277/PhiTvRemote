package com.phicomm.remotecontrol.fragments.screenshot;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.remotecontrol.BuildConfig;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.util.LogUtil;


/**
 * Created by xufeng02.zhou on 2017/7/17.
 */

public class ScreenshotFragment extends Fragment implements ScreenshotContract.View {
    final static String TAG = "screenshot";

    ScreenshotContract.Presenter mPresenter;
    ImageView mImageView;

    public ScreenshotFragment() {
        setPresenter(new ScreenshotPresenter(this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LogUtil.d(TAG, "onViewCreated");
        return inflater.inflate(R.layout.fragment_screenshot, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onViewCreated");
        TextView version = (TextView) view.findViewById(R.id.tv_version);
        version.setText("Curent Version:" + BuildConfig.VERSION_NAME);

        mImageView = (ImageView) view.findViewById(R.id.iv_preview);
        Button button = (Button) view.findViewById(R.id.btn_screenshot);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.doScreenshot();
            }
        });

        Button saveBtn = (Button) view.findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.savePicture();
            }
        });
    }


    @Override
    public void setPresenter(ScreenshotContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.setView(this);
    }

    @Override
    public void showPicture(Drawable drawable) {
        mImageView.setImageDrawable(drawable);
    }
}
