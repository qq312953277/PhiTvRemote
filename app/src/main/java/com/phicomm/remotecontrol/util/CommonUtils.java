package com.phicomm.remotecontrol.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.phicomm.remotecontrol.ConnectManager;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.RemoteBoxDevice;
import com.phicomm.remotecontrol.base.BaseApplication;

import java.io.File;
import java.util.Locale;

public class CommonUtils {
    private static Toast toast;
    private static AlertDialog loadingdialog;
    private static AlertDialog loadedfailed;//加载失败
    private static String CACHE_FILE_NAME = "datacache";

    public static int getDensity(Context context) {
        return (int) context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void startIntent(Context context, Intent extras, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }


    /**
     * 带动画的跳转
     *
     * @param activity
     * @param clazz
     */
    public static void startIntent(Activity activity, Class clazz) {
        BaseApplication.getApplication().remove(activity);
        activity.startActivity(new Intent(activity, clazz));
        //activity.finish();
        //实现淡入浅出的效果
        activity.overridePendingTransition(R.animator.alpha_anim_in, R.animator.alpha_anim_out);
    }

    public static void showToastCenter(String str) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getContext(), str, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        }
        toast.setText(str);
        toast.show();
    }

    public static void showToastCenter(Context context, String str) {
        if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        }
        toast.setText(str);
        toast.show();
    }

    public static void showToastBottom(String str) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getContext(), str, Toast.LENGTH_SHORT);
        }
        toast.setText(str);
        toast.show();
    }

    public static void showToastCenter(Context context, int strID) {
        if (toast == null) {
            toast = Toast.makeText(context, strID, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        }
        toast.setText(context.getString(strID));
        toast.show();
    }

    public static void showLongToast(Context context, String str) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showShortToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getContext(), str, Toast.LENGTH_SHORT);
        }
        toast.setText(str);
        toast.show();
    }

    public static void showDialog(Context context, String str, final DialogClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setMessage(str);
        dialog.setButton(BaseApplication.getContext().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
                if (null != listener)
                    listener.onClick();
            }
        });
        dialog.show();
    }

    public static void showDialog(Context context, String str) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setMessage(str);
        dialog.setButton(BaseApplication.getContext().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        dialog.show();
    }

    public interface DialogClickListener {
        void onClick();
    }

    public static int getScreenWidth(Activity a) {
        DisplayMetrics metric = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 打开软键盘
     */
    public static void openKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeyboard(Context context) {
        View view = ((Activity) context).getWindow().peekDecorView();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getAppFIlePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".ody" + File.separator;
        return path;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @param context
     * @param color
     * @return
     */
    public static int getColor(Context context, int color) {
        return context.getResources().getColor(color);
    }

    public static long getCacheSize(Context context) {
        File cacheFile = new File(context.getFilesDir().getParent() + "/shared_prefs/" + CACHE_FILE_NAME + ".xml");
        if (cacheFile.exists()) {
            return cacheFile.length();
        }
        return 0;
    }

    public static String getAppChannel() {
        Context context = BaseApplication.getContext();
        String value = null;
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo != null) {
            value = appInfo.metaData.getString("APP_CHANNEL");
        }
        return value;
    }


    /**
     * 获取密码强度
     *
     * @param pwd
     * @return
     */
    public static int getPasswordStrength(String pwd) {
        String fuhao = "\\W";
        if (pwd.matches("^\\d+$") || pwd.matches("^[a-zA-Z]+$") || pwd.matches("^[" + fuhao + "]+$")) {
            return 1;
        } else if (pwd.matches("^[0-9A-Za-z]+$") || pwd.matches("^[0-9" + fuhao + "]+$") || pwd.matches("^[A-Za-z" + fuhao + "]+$")) {
            return 2;
        } else if (pwd.matches("^[0-9A-Za-z" + fuhao + "]+$")) {
            return 3;
        } else {
            return -1;
        }
    }

    /**
     * base64ToBitmap
     *
     * @param base64String
     * @return
     */
    public static Bitmap base64ToBitmap(String base64String) {
        if (StringUtils.isNull(base64String)) {
            return null;
        }
        try {
            byte[] bytes = Base64.decode((String) base64String, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getCurrentUrl() {
        String url = null;
        RemoteBoxDevice CurrentDevice = ConnectManager.getInstance().getConnectingDevice();
        if (CurrentDevice != null) {
            url = "http://" + CurrentDevice.getAddress() + ":" + CurrentDevice.getPort() + "/v1/";
        }
        return url;
    }

    public static String getSystemLanguage() {
        Locale locale = BaseApplication.getContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language;
    }
}
