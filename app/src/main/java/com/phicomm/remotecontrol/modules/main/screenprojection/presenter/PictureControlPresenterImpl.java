package com.phicomm.remotecontrol.modules.main.screenprojection.presenter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.phicomm.remotecontrol.base.BaseApplication;
import com.phicomm.remotecontrol.modules.main.screenprojection.activities.PictureControlView;
import com.phicomm.remotecontrol.modules.main.screenprojection.fragments.PictureFragment;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaContentBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaControlBiz;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.model.item.Item;

/**
 * Created by kang.sun on 2017/8/31.
 */
public class PictureControlPresenterImpl implements PictureControlPresenter {
    private PictureControlView mView;
    private BaseApplication mBaseApplication;
    private Item item;
    protected MediaControlBiz controlBiz;
    private long mId;
    private int index;

    public PictureControlPresenterImpl(PictureControlView mView, BaseApplication mBaseApplication) {
        this.mView = mView;
        this.mBaseApplication = mBaseApplication;
        Device device = mBaseApplication.getDeviceDisplay().getDevice();
        item = mBaseApplication.getItem();
        mId = 0;
        controlBiz = new MediaControlBiz(device, mId);
        for (int i = 0; i < MediaContentBiz.mPictureItemArrayList.get(PictureFragment.mAlbumIndex).getPictureItemList().size(); i++) {
            if (item.getId().equals(MediaContentBiz.mPictureItemArrayList.get(PictureFragment.mAlbumIndex).getPictureItemList().get(i).getId())) {
                index = i;
            }
        }
    }

    @Override
    public void showPicture(ImageView imageView) {
        mView.setTittle(item.getTitle());
        controlBiz.setPlayUri(item);
        String pictureValues = MediaContentBiz.mPictureMapList.get(item.getId());
        Glide.with(BaseApplication.getContext()).load(pictureValues).into(imageView);
    }

    @Override
    public void showPrePicture(ImageView imageView) {
        if (index > 0) {
            index--;
            if (index >= 0) {
                Item item = MediaContentBiz.mPictureItemArrayList.get(PictureFragment.mAlbumIndex).getPictureItemList().get(index);
                String pictureValues = MediaContentBiz.mPictureMapList.get(item.getId());
                Glide.with(BaseApplication.getContext()).load(pictureValues).into(imageView);
                mView.setTittle(item.getTitle());
                controlBiz.setPlayUri(item);
            }
        } else {
            mView.showMessage("已经是第一张图片");
        }
    }

    @Override
    public void showNextPicture(ImageView imageView) {
        if (index < MediaContentBiz.mPictureItemArrayList.get(PictureFragment.mAlbumIndex).getPictureItemList().size()) {
            index++;
            if (index < MediaContentBiz.mPictureItemArrayList.get(PictureFragment.mAlbumIndex).getPictureItemList().size()) {
                Item item = MediaContentBiz.mPictureItemArrayList.get(PictureFragment.mAlbumIndex).getPictureItemList().get(index);
                String pictureValues = MediaContentBiz.mPictureMapList.get(item.getId());
                Glide.with(BaseApplication.getContext()).load(pictureValues).into(imageView);
                mView.setTittle(item.getTitle());
                controlBiz.setPlayUri(item);
            }
        } else {
            mView.showMessage("已经是最后一张图片");
        }
    }
}
