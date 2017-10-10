package com.phicomm.remotecontrol.modules.main.screenprojection.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.PictureControlView;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaContentBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaControlBiz;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.model.item.Item;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kang.sun on 2017/8/31.
 */
public class PictureControlPresenterImpl implements PictureControlPresenter {
    private static String TAG = "PictureControlPresenterImpl";
    private PictureControlView mView;
    private BaseApplication mBaseApplication;
    private Item item;
    protected MediaControlBiz controlBiz;
    private long mId; // item播放实例id
    private String imageurl;
    private int index;

    public PictureControlPresenterImpl(PictureControlView mView, BaseApplication mBaseApplication) {
        this.mView = mView;
        this.mBaseApplication = mBaseApplication;
        Device device = mBaseApplication.getDeviceDisplay().getDevice();
        item = mBaseApplication.getItem();
        imageurl = item.getFirstResource().getValue();
        mId = 0;
        controlBiz = new MediaControlBiz(device, mId);
        for (int i = 0; i < MediaContentBiz.mPictureItemArrayList.get(LocalMediaItemPresenterImpl.mAlbumIndex).getPictureItemList().size(); i++) {
            if (item.getId().equals(MediaContentBiz.mPictureItemArrayList.get(LocalMediaItemPresenterImpl.mAlbumIndex).getPictureItemList().get(i).getId())) {
                index = i;
            }
        }
    }

    @Override
    public void showPicture() {
        mView.setTittle(item.getTitle());
        controlBiz.setPlayUri(item);//投屏
        new LoadImageAsyncTask().execute(imageurl);//本地展示
    }

    @Override
    public void showPrePicture() {
        if (index > 0) {
            index--;
            if (index >= 0) {
                Item item = MediaContentBiz.mPictureItemArrayList.get(LocalMediaItemPresenterImpl.mAlbumIndex).getPictureItemList().get(index);
                new LoadImageAsyncTask().execute(item.getFirstResource().getValue());
                mView.setTittle(item.getTitle());
                controlBiz.setPlayUri(item);
            }
        } else {
            mView.showMessage("已经是第一张图片");
        }
    }

    @Override
    public void showNextPicture() {
        if (index < MediaContentBiz.mPictureItemArrayList.get(LocalMediaItemPresenterImpl.mAlbumIndex).getPictureItemList().size()) {
            index++;
            if (index < MediaContentBiz.mPictureItemArrayList.get(LocalMediaItemPresenterImpl.mAlbumIndex).getPictureItemList().size()) {
                Item item = MediaContentBiz.mPictureItemArrayList.get(LocalMediaItemPresenterImpl.mAlbumIndex).getPictureItemList().get(index);
                new LoadImageAsyncTask().execute(item.getFirstResource().getValue());
                mView.setTittle(item.getTitle());
                controlBiz.setPlayUri(item);
            }
        } else {
            mView.showMessage("已经是最后一张图片");
        }
    }

    class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.showDialog();
        }

        @Override
        protected Bitmap doInBackground(String... params) {//可变长度的数组,参数从excute()方法中传入
            String url = params[0];//获取url地址
            Bitmap bitmap = null;
            URLConnection urlConnection;
            InputStream inputStream;
            BufferedInputStream bufferedInputStream;
            try {
                urlConnection = new URL(url).openConnection();
                inputStream = urlConnection.getInputStream();
                bufferedInputStream = new BufferedInputStream(inputStream);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);//解析输入流得到bitmap
                bufferedInputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mView.showPicture(bitmap);
            mView.dismissDialog();
        }
    }
}
