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
                case FiletypeUtil.FILETYPE_MUSIC:
                    holder.setImageResource(R.id.iv_icon,
                            R.drawable.file_audio_icon);
                    break;
                case FiletypeUtil.FILETYPE_PIC:
                    holder.setImageResource(R.id.iv_icon,
                            R.drawable.file_image_icon);
                    break;
            }
        }
        holder.setText(R.id.tv_title, contentTitle);
    }
}

