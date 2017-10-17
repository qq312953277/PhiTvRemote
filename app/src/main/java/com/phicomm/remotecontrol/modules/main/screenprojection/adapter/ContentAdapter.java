package com.phicomm.remotecontrol.modules.main.screenprojection.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.model.MediaContentBiz;
import com.phicomm.remotecontrol.modules.main.screenprojection.utils.FiletypeUtil;

import org.fourthline.cling.support.model.item.Item;

import java.util.List;


/**
 * Created by kang.sun on 2017/8/22.
 */

public class ContentAdapter extends GeneralAdapter<ContentItem> {
    public static final int TITTLE_LIMIT_LENGTH = 25;
    public static final int TITTLE_FRONT_LENGTH = 20;
    public static final int TITTLE_BACK_LENGTH = 4;

    public ContentAdapter(Context ctx, int resource, List<ContentItem> data) {
        super(ctx, resource, data);
    }

    @Override
    public void convert(GeneralAdapter.ViewHolder holder, ContentItem item, int position) {
        String contentTitle;
        if (item.isContainer()) {
            contentTitle = item.getContainer().getTitle();
            ((holder.getView(R.id.tv_title))).setVisibility(View.VISIBLE);
            setTittle(holder, R.id.tv_title, contentTitle);
        } else {
            Item it = item.getItem();
            contentTitle = it.getTitle();
            switch (item.getFiletype()) {
                case FiletypeUtil.FILETYPE_MOVIE:
                    String movieValues = MediaContentBiz.mVideoMapList.get(it.getId());
                    Glide.with(getContext()).load("file://" + movieValues).dontAnimate()
                            .error(R.drawable.album_default_loading_pic).centerCrop()
                            .placeholder(R.drawable.album_default_loading_pic)
                            .priority(Priority.HIGH).into((ImageView) (holder.getView(R.id.iv_icon)));
                    ((holder.getView(R.id.item_layout))).setVisibility(View.VISIBLE);
                    setTittle(holder, R.id.tv_videotitle, contentTitle);
                    setTittle(holder, R.id.tv_time, it.getFirstResource().getDuration());
                    break;
                case FiletypeUtil.FILETYPE_PIC:
                    ((holder.getView(R.id.tv_title))).setVisibility(View.VISIBLE);
                    String pictureVlaues = MediaContentBiz.mPictureMapList.get(it.getId());
                    Glide.with(getContext()).load("file://" + pictureVlaues).dontAnimate()
                            .error(R.drawable.album_default_loading_pic).centerCrop()
                            .placeholder(R.drawable.album_default_loading_pic)
                            .priority(Priority.HIGH).into((ImageView) (holder.getView(R.id.iv_icon)));
                    setTittle(holder, R.id.tv_title, contentTitle);
                    break;
            }
        }
    }

    private void setTittle(GeneralAdapter.ViewHolder holder, int id, String contentTitle) {
        if (contentTitle.length() <= TITTLE_LIMIT_LENGTH) {
            holder.setText(id, contentTitle);
        } else {
            String mShortContentTitle = contentTitle.substring(0, TITTLE_FRONT_LENGTH) + "..." + contentTitle.substring(contentTitle.length() - TITTLE_BACK_LENGTH, contentTitle.length());
            holder.setText(id, mShortContentTitle);
        }
    }
}

