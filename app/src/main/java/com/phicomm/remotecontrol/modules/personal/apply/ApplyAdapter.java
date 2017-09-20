package com.phicomm.remotecontrol.modules.personal.apply;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseRecycleAdapter;
import com.phicomm.remotecontrol.util.CommonUtils;
import com.phicomm.remotecontrol.util.ImageFactory;

import butterknife.BindView;

/**
 * Created by hao04.wu on 2017/9/18.
 */

public class ApplyAdapter extends BaseRecycleAdapter {

    public static final int TYPE_TITLE = 0xff01;
    public static final int TYPE_ITEM = 0xff02;

    public ApplyAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutResId(int viewType) {
        if (viewType == TYPE_TITLE) {
            return R.layout.item_apply_header;
        }
        return R.layout.item_apply;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TITLE;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public void bindItemViewType(BaseViewHolder holder, final int position) {
        if (holder instanceof ApplyTitleViewHolder) {
            return;
        }

        if (holder instanceof ApplyItemViewHolder) {
            int mPosition = position - 1;
            ApplyItemViewHolder viewHolder = (ApplyItemViewHolder) holder;
            if (mDataList != null && mDataList.size() > 0) {
                final ApplyInfosBean.AppInfo info = (ApplyInfosBean.AppInfo) mDataList.get(mPosition);
                if (info != null && mItemViewClickListener != null) {
                    viewHolder.mTvAppName.setText(info.name + "");
                    viewHolder.mIvIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemViewClickListener.onItemViewClick(info, position);
                        }
                    });
                    String url = CommonUtils.getCurrentUrl() + "appicon?appid=" + info.appid;
                    ImageLoader.getInstance().displayImage(url
                            , viewHolder.mIvIcon, ImageFactory.getDefaultImageOptions(R.mipmap.ic_launcher));
                }
            }
        }

    }

    @Override
    public BaseViewHolder getViewHolder(int viewType, View view) {
        if (viewType == TYPE_TITLE) {
            return new ApplyTitleViewHolder(view);
        }
        return new ApplyItemViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    int result = 1;
                    switch (type) {
                        case TYPE_TITLE:
                            result = gridLayoutManager.getSpanCount();
                            break;
                        case TYPE_ITEM:
                            result = 1;
                            break;
                        default:
                            break;
                    }
                    return result;
                }
            });
        }
    }

    public class ApplyItemViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_app_icon)
        ImageView mIvIcon;

        @BindView(R.id.tv_app_name)
        TextView mTvAppName;

        public ApplyItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ApplyTitleViewHolder extends BaseViewHolder {
        public ApplyTitleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
