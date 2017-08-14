package com.phicomm.remotecontrol.modules.main.screenshot;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.phicomm.remotecontrol.R;
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

public class ScreenshotPresenterImp implements ScreenshotPresenter {
    private ScreenshotView mView;
    private Context mContext;
    byte[] mPicByte;

    public ScreenshotPresenterImp(ScreenshotView mView, Context mContext) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void doScreenshot() {

        TaskQuene.getInstance().doScreenshot(new PhiCallBack<byte[]>() {
            @Override
            public void onSuccess(byte[] picByte) {
                mPicByte = picByte;
                ByteArrayInputStream inputStream = new ByteArrayInputStream(picByte);
                Drawable drawable = Drawable.createFromResourceStream(
                        mContext.getResources(),
                        null,
                        inputStream,
                        "screenshot");
                mView.showPicture(drawable);
                mView.showMessage(mContext.getString(R.string.success_screenshot));
            }

            @Override
            public void onFailure(String msg) {
                mView.onFailure(msg);
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
                mView.showMessage(mContext.getString(R.string.success_save));
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
        }else {
            mView.showMessage(mContext.getString(R.string.fail_save));
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