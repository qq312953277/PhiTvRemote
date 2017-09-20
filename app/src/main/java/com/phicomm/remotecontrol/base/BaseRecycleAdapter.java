package com.phicomm.remotecontrol.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by hao04.wu on 2017/6/12.
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.BaseViewHolder> {
    public Context mContext;
    public List<T> mDataList;
    /**
     * Item点击事件回调
     */
    public OnItemViewClickListener mItemViewClickListener;
    /**
     * Item长按事件回调
     */
    public OnItemViewLongClickListener onItemViewLongClickListener;

    public BaseRecycleAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();

    }

    /**
     * 获取Item布局资源文件ID
     *
     * @return 资源文件ID
     */
    public abstract int getItemLayoutResId(int viewType);

    /**
     * 绑定Item view及设置显示数据
     *
     * @param holder
     * @param position
     */
    public abstract void bindItemViewType(BaseViewHolder holder, int position);

    /**
     * 获取对应的布局view
     *
     * @param view
     * @return
     */
    public abstract BaseViewHolder getViewHolder(int viewType,View view);

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getItemLayoutResId(viewType), parent, false);
        return getViewHolder(viewType,view);
    }

    @Override
    public void onBindViewHolder(BaseRecycleAdapter.BaseViewHolder holder, int position) {
        bindItemViewType(holder, position);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setData(List<T> data) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList = data;
        notifyDataSetChanged();
    }

    public void addItemes(ArrayList<T> item) {
        mDataList.addAll(item);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        mDataList.add(item);
        notifyItemInserted(mDataList.size() - 1);
    }

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        mItemViewClickListener = onItemViewClickListener;
    }

    /**
     * Item View点击事件监听器
     */
    public interface OnItemViewClickListener<T> {
        void onItemViewClick(T object,int position);
    }

    /**
     * Item View长按事件监听器
     */
    public interface OnItemViewLongClickListener {
        void onLongClick(View view, int position);
    }
}
