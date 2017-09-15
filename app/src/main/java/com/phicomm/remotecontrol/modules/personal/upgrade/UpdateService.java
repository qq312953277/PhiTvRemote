package com.phicomm.remotecontrol.modules.personal.upgrade;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.phicomm.remotecontrol.BuildConfig;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.preference.PreferenceDef;
import com.phicomm.remotecontrol.preference.PreferenceRepository;
import com.phicomm.remotecontrol.util.CommonUtils;

import java.io.File;

/**
 * Created by hk on 2016/10/12.
 */
public class UpdateService extends Service {

    private DownloadManager downloadManager;
    private long downloadId;
    private DownloadCompleteReceiver receiver;//接收下载完的广播
    private String hint;
    public static final String DOWNLOAD_URL = "download_url";
    public static final String DOWNLOAD_NAME = "download_name";
    private static final String DirName = "PhiTvRemote";
    private PreferenceRepository mPreference;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra(DOWNLOAD_URL);
        String versionName = intent.getStringExtra(DOWNLOAD_NAME);
        mPreference = new PreferenceRepository(getApplicationContext());
        String path = Environment.getExternalStoragePublicDirectory(DirName) + File.separator + versionName;
        File file = new File(path);
        hint = getApplicationContext().getResources().getString(R.string.download_wait);

        if (!TextUtils.isEmpty(url)) {
            if (isNeedDownloadAgain()) {
                Toast.makeText(getApplicationContext(), hint, Toast.LENGTH_SHORT).show();
                // 调用下载
                initDownManager(url, versionName);
            }
        }
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        // 注销下载广播
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    /**
     * 初始化下载器
     **/
    private void initDownManager(String url, String versionName) {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();

        //设置下载地址
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        Log.v("url:", url);
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);

        request.setMimeType("application/vnd.android.package-archive");

        // 下载时，通知栏显示途中
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        // 显示下载界面
        request.setVisibleInDownloadsUi(true);

        // 设置下载后文件存放的位置，注意7.0使用setDestinationInExternalPublicDir，而不是setDestinationInExternalFilesDir
        request.setDestinationInExternalPublicDir(DirName, DirName + "-" + versionName);

        request.setTitle(DirName + "-" + versionName);
        request.setDescription(getApplicationContext().getResources().getString(R.string.update_description));

        // 将下载请求放入队列
        downloadId = downloadManager.enqueue(request);
        mPreference.put(PreferenceDef.APP_VERSION, PreferenceDef.VERSION_DOWNLOAD_ID, downloadId);
        //注册下载广播
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private boolean isNeedDownloadAgain() {
        boolean isNeedDownloadAgain = true;
        DownloadManager.Query query = new DownloadManager.Query();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        long id = (long) mPreference.get(PreferenceDef.APP_VERSION, PreferenceDef.VERSION_DOWNLOAD_ID, -1L);

        if (id != -1) {
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columnIndex);
                int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                int reason = cursor.getInt(columnReason);
                switch (status) {
                    case DownloadManager.STATUS_FAILED:
                        isNeedDownloadAgain = true;
                        break;
                    case DownloadManager.STATUS_PAUSED:
                        isNeedDownloadAgain = false;
                        break;
                    case DownloadManager.STATUS_PENDING:
                        isNeedDownloadAgain = false;
                        break;
                    case DownloadManager.STATUS_RUNNING:
                        Toast.makeText(getApplicationContext(), hint, Toast.LENGTH_SHORT).show();
                        isNeedDownloadAgain = false;
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        //the download has successfully completed
                        isNeedDownloadAgain = true;
                        //  installApk(id, downloadManager, mContext);
                        break;

                }
            }

        }

        return isNeedDownloadAgain;
    }


    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                //获取下载的文件id
                long completeDownloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Uri uri = downloadManager.getUriForDownloadedFile(completeDownloadId);
                if (uri != null) {
                    //自动安装apk
                    installAPK(downloadManager.getUriForDownloadedFile(completeDownloadId), context);
                } else {
                    String hint = getApplicationContext().getResources().getString(R.string.download_failure);
                    Toast.makeText(getApplicationContext(), hint, Toast.LENGTH_SHORT).show();
                }
                //停止服务并关闭广播
                UpdateService.this.stopSelf();

            }
        }

        /**
         * 安装apk文件
         */
        private void installAPK(Uri uri, Context context) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            File apkFile = queryDownloadedApk();
            String hint = getApplicationContext().getResources().getString(R.string.file_not_exist);
            try {
                if (apkFile.exists()) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        //7.0系统需要在AndroidManifest.xml里配置provider固定不变
                        Uri uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", apkFile);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    }
                } else {
                    CommonUtils.showShortToast(hint);
                }
                startActivity(intent);
            } catch (Exception e) {
                CommonUtils.showShortToast(hint);

            }
        }

        /**
         * 解决6.0,7.0系统以上安装问题，方法一：
         * Try to return the absolute file path from the given Uri
         *
         * @param context
         * @param uri
         * @return the file path or null
         * 此方法可以正确解析url对应的真实路径
         */
        public String getRealFilePath(final Context context, final Uri uri) {
            if (null == uri) return null;
            final String scheme = uri.getScheme();
            String path = null;
            if (scheme == null)
                path = uri.getPath();
            else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                path = uri.getPath();
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        if (index > -1) {
                            path = cursor.getString(index);
                        }
                    }
                    cursor.close();
                }
            }
            return path;
        }

        /**
         * 解决6.0,7.0系统以上安装问题，方法二：
         * Try to return the absolute file path from the given Uri
         *
         * @return the file path or null
         * 此方法可以正确解析url对应的真实路径文件
         */
        public File queryDownloadedApk() {
            File targetApkFile = null;
            if (downloadId != -1) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
                Cursor cur = downloadManager.query(query);
                if (cur != null) {
                    if (cur.moveToFirst()) {
                        String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        if (!TextUtils.isEmpty(uriString)) {
                            targetApkFile = new File(Uri.parse(uriString).getPath());
                        }
                    }
                    cur.close();
                }
            }
            return targetApkFile;
        }
    }

}