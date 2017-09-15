package com.phicomm.remotecontrol.util;

import android.content.Context;
import android.view.WindowManager;

import com.phicomm.widgets.alertdialog.PhiLoadDialog;

/**
 * Created by yun.wang
 * Date :2017/6/30
 * Description: ***
 * Version: 1.0.0
 */
public class DialogUtils {
    private static PhiLoadDialog sDialog;

    public static void showLoadingDialog(Context context, int msgRes) {
        dismissDialog();
        sDialog = new PhiLoadDialog(context, false);
        sDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.setTitle(msgRes);
        sDialog.show();
    }

    public static void showLoadingDialog(Context context) {
        dismissDialog();
        sDialog = new PhiLoadDialog(context, false);
        sDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.show();

    }

    public static void showLoadingDialog(Context context, String msg) {
        dismissDialog();
        sDialog = new PhiLoadDialog(context, false);
        sDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.setTitle(msg);
        sDialog.show();
    }

    public static void cancelLoadingDialog() {
        if (sDialog != null) {
            sDialog.dismiss();
            sDialog = null;
        }
    }

    public static final void dismissDialog() {
        if (sDialog != null) {
            sDialog.dismiss();
            sDialog = null;
        }
    }

    public static final void showProgressDialog(Context context) {
        dismissDialog();
        sDialog = new PhiLoadDialog(context, false);
        sDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.show();
    }
}
