package com.phicomm.remotecontrol.modules.main.screenprojection.adapter;

import android.content.Context;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.modules.main.screenprojection.entity.ContentItem;
import com.phicomm.remotecontrol.modules.main.screenprojection.utils.FiletypeUtil;

import org.fourthline.cling.support.model.item.Item;

import java.util.List;

/**
 * Created by kang.sun on 2017/8/22.
 */

public class ContentAdapter extends GeneralAdapter<ContentItem> {
    public static final int TITTLE_LIMIT_LENGTH = 28;
    public static final int TITTLE_FRONT_LENGTH = 15;
    public static final int TITTLE_BACK_LENGTH = 4;

    public ContentAdapter(Context ctx, int resource, List<ContentItem> data) {
        super(ctx, resource, data);
    }

    @Override
    public void convert(GeneralAdapter.ViewHolder holder, ContentItem item, int position) {
        String contentTitle;
        if (item.isContainer()) {
            contentTitle = item.getContainer().getTitle();
        } else {
            Item it = item.getItem();
            contentTitle = it.getTitle();
            switch (item.getFiletype()) {
                case FiletypeUtil.FILETYPE_MOVIE:
                    holder.setImageResource(R.id.iv_icon,
                            R.drawable.file_video_icon);
                    break;
                case FiletypeUtil.FILETYPE_PIC:
                    holder.setImageResource(R.id.iv_icon,
                            R.drawable.file_image_icon);
                    break;
            }
        }
        if (contentTitle.length() <= TITTLE_LIMIT_LENGTH) {
            holder.setText(R.id.tv_title, contentTitle);
        } else {
            String mShortContentTitle = contentTitle.substring(0, TITTLE_FRONT_LENGTH) + "..." + contentTitle.substring(contentTitle.length() - TITTLE_BACK_LENGTH, contentTitle.length());
            holder.setText(R.id.tv_title, mShortContentTitle);
        }
    }
}

