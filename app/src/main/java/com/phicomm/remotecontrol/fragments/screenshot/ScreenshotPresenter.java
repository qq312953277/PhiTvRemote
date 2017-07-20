package com.phicomm.remotecontrol.fragments.screenshot;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.app.Fragment;

import com.phicomm.remotecontrol.TaskQuene;
import com.phicomm.remotecontrol.httpclient.PhiCallBack;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.phicomm.remotecontrol.constant.Configs.SCRENSHOT_DIR;
import static com.phicomm.remotecontrol.constant.Configs.SCRENSHOT_FORMAT;
import static com.phicomm.remotecontrol.constant.Configs.SCRENSHOT_PREFIX;
import static com.phicomm.remotecontrol.constant.Configs.SCRENSHOT_SUFFIX;

/**
 * Created by xufeng02.zhou on 2017/7/18.
 */

public class ScreenshotPresenter implements ScreenshotContract.Presenter {
    ScreenshotContract.View mView;
    Fragment mParent;
    byte[] mPicByte;

    ScreenshotPresenter(Fragment parent) {
        mParent = parent;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setView(ScreenshotContract.View view) {
        mView = view;
    }

    @Override
    public void doScreenshot() {

        TaskQuene.getInstance().doScreenshot(new PhiCallBack<byte[]>() {
            @Override
            public void onSuccess(byte[] picByte) {
                mPicByte = picByte;
                ByteArrayInputStream inputStream = new ByteArrayInputStream(picByte);
                Drawable drawable = Drawable.createFromResourceStream(
                        mParent.getResources(),
                        null,
                        inputStream,
                        "screenshot");
                mView.showPicture(drawable);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void savePicture() {
        File file = getScreenshotStorePath();
        if (file != null && mPicByte != null) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                out.write(mPicByte);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mPicByte = null;
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private File getScreenshotStorePath() {
        try {
            File rootdir = Environment.getExternalStorageDirectory();
            File dir = new File(rootdir, Environment.DIRECTORY_PICTURES + "/" + SCRENSHOT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, getFileName());
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    private String getFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat(SCRENSHOT_FORMAT);
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);

        return SCRENSHOT_PREFIX + str + SCRENSHOT_SUFFIX;
    }
}